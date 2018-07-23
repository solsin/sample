package processor.csv;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;

import com.opencsv.CSVReader;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import lombok.extern.slf4j.Slf4j;

/**
 * opencsv�� csv to bean mapping�� setId(Serializable id)�� PropertyEditor�� writable method�� ó������ ���Ѵ�.
 * �̸� fix�ϱ� ����, HeaderColumnNameMappingStrategy�� caputerHeader �޼��� ���� �� csv�� ID column�� �����ϸ� 
 * setId(Long id)�� writable method�� ����Ѵ�.
 * 
 * @author Chulhui Park <chulhui72@gmail.com>
 */
@Slf4j
public class CustomHeaderColumnNameMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {

	@Override
	public void captureHeader(CSVReader reader) throws IOException, CsvRequiredFieldEmptyException {
		super.captureHeader(reader);
		log.info("finish capture header");

		// spring data�� AbstracctPersistable�� ��ӹ޾�, setId�� unwritable method�� ��� ���� ����
		PropertyDescriptor descriptior = descriptorMap.get("ID");
		if (descriptior == null) {
			return;
		}
		if (descriptior.getWriteMethod() == null) {
			log.info("force PropertyDescriptor.writerMehtod of ID");
			Method method;
			try {
				method = getType().getMethod("setId", Long.class);
				descriptior.setWriteMethod(method);
			} catch (Exception e) {
				log.warn("error occured during writerMehtod of PropertyDescriptior for ID:{}", e.getMessage(), e);
			}
		}
	} 

}
