package processor;

import java.beans.IntrospectionException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

import config.StandAloneConfig;
import lombok.extern.slf4j.Slf4j;
import processor.csv.CommentSkipCsvReader;
import processor.csv.CustomHeaderColumnNameMappingStrategy;
import sample.config.JPAConfig;
import sample.entities.AbstractPersistable;

/**
 * CSV format�� �����͸� JPA entity�� �����Ͽ� �ϰ� ����ϴ� processor 1��° ���ο� column name�� �ݵ��
 * �־�� �Ѵ�. id column�� ���� ��� id�� entity�� ���ǵ� sequence�� ���� �ڵ� �����ȴ�. id column�� ������
 * ��� �ϰ� ��� ��, id�� ������ update�Ѵ�.
 * 
 * @author Chulhui Park <chulhui72@plgrim.com>
 */
@Component
@Slf4j
public class CSVFormatProcessor {
	private static final String PREFIX_CLASS_IN_COMMENT = "#class:";
	private static String CSV_FILE_EXTENSION = ".csv";
	
	@Autowired
	ApplicationContext ctx;

	@Autowired
	EntityManager em;

	private Collection<PK> updateList = new LinkedList<>();

	private String processRootDirectory;
	private String pathDelimeter = File.separator;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	public void process(Class clazz, String location) throws FileNotFoundException, IOException,
			NoSuchMethodException, SecurityException, IntrospectionException {
		assert location != null;

		Resource resource = ctx.getResource(location);
		try (Reader reader = new FileReader(resource.getFile())) {
			CustomHeaderColumnNameMappingStrategy<?> strategy = new CustomHeaderColumnNameMappingStrategy<>();
			strategy.setType(clazz);

			CsvToBean csvToBean = new CsvToBeanBuilder(reader).withMappingStrategy(strategy)
					.withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_QUOTES).withIgnoreLeadingWhiteSpace(true)
					.build();

			csvToBean.setCsvReader(new CommentSkipCsvReader(reader));
			Iterator iter = csvToBean.iterator();
			while (iter.hasNext()) {
				AbstractPersistable<Long> obj = (AbstractPersistable<Long>) iter.next();
				if (obj.getId() == null) {
					em.persist(obj);
					log.info("EntityManager.persist: {}", obj);
				} else {
					log.info("original id:{}", obj.getId());
					AbstractPersistable<Long> merged = em.merge(obj);
					if (!obj.getId().equals(merged.getId())) {
						PK pk = new PK(merged.getId(), obj.getId());
						updateList.add(pk);
					}
				}
			}
			em.flush();

			updateId(getTableName(clazz));
		}
	}

	public void process(File rootDirectory) {
		assert rootDirectory.exists();
		assert rootDirectory.isDirectory();
		if (processRootDirectory == null) {
			processRootDirectory = rootDirectory.getAbsolutePath();
		}

		for (File file : rootDirectory.listFiles()) {
			if (file.isDirectory()) {
				process(file);
				continue;
			}
			if (!file.getName().endsWith(CSV_FILE_EXTENSION)) {
				log.info("skip non csv file:{}", file.getAbsolutePath());
				continue;
			}

			String classPath = getClassName(file);
			log.info("path:{}", classPath);
			try {
				Class<?> clazz = ctx.getClassLoader().loadClass(classPath);
				String fileUri = "file://"+file.getCanonicalPath();
				process(clazz, fileUri);
			} catch (Exception e) {
				log.warn("skip this path[{}] due to error:{}", classPath, e.getMessage(), e);
				continue;
			}
		}
	}

	private void updateId(String tableName) {
		if (updateList == null || updateList.size() == 0) {
			return;
		}

		String sql = "UPDATE " + tableName + " SET id=? WHERE id=?";
		Query query = em.createNativeQuery(sql);
		for (PK pk : updateList) {
			query.setParameter(1, pk.updateId);
			query.setParameter(2, pk.id);
			int res = query.executeUpdate();
			log.info("table[{}].id[{}] is updated to [{}], updated row[{}]", tableName, pk.id, pk.updateId, res);
		}
		em.flush();
	}

	private String getTableName(Class<?> clazz) {
		Table t = clazz.getAnnotation(Table.class);
		if (t == null) {
			return clazz.getName();
		}
		return t.name();
	}

	/**
	 * file�� ù��° line�� #class: �� �����ϴ� ��� �� ������ class�� �����ϰ�, ������ file path�� ���� class
	 * ������ �����ؼ� �����Ѵ�.
	 */
	private String getClassName(File file) {
		//file�� ù��° ���ο� #class: �� �����ϴ��� �˻�
		try {
			LineIterator it = IOUtils.lineIterator(new BufferedReader(new FileReader(file)));
			String line = it.next().trim();
			if (line.startsWith(PREFIX_CLASS_IN_COMMENT)) {
				line = line.substring(PREFIX_CLASS_IN_COMMENT.length()).trim();
				return line;
			}
		} catch (Exception e) {
			log.warn("skip to read class name from .csv file:{}", e.getMessage(), e);
		}

		// file path�� ���� class ���� ����
		String path = file.getAbsolutePath().substring(processRootDirectory.length() + 1);
		path = path.substring(0, path.length() - CSV_FILE_EXTENSION.length());
		path = StringUtils.replace(path, pathDelimeter, ".");
		return path;
	}

	class PK {
		long id;
		long updateId;

		PK(long id, long updateId) {
			this.id = id;
			this.updateId = updateId;
		}
	}
}

