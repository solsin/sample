package processor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import config.StandAloneConfig;
import sample.config.JPAConfig;

public class DBProcessor {
	
	private static ApplicationContext context;
	
	private static void initSpringContext() throws ClassNotFoundException, IOException {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass("sample.entities.Sample");
		
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
		Properties prop = new Properties();
		prop.load(in);
		in.close();
		PropertiesPropertySource pps = new PropertiesPropertySource("application.properties", prop);
		
		ConfigurableEnvironment env = context.getEnvironment();
		env.getPropertySources().addFirst(pps);
		context.setEnvironment(env);
		context.register(StandAloneConfig.class);
		context.register(JPAConfig.class);
		context.refresh();
		
		DBProcessor.context = context;
	}
	
	public static void initDB(String filePath) throws ClassNotFoundException, IOException {
		File file = new File(filePath);
		assert file.exists();
		initDB(file);
	}

	public static void initDB(File file) throws ClassNotFoundException, IOException {
		if (context == null) {
			initSpringContext();
		}
		
		CSVFormatProcessor processor = context.getBean(CSVFormatProcessor.class);
		PlatformTransactionManager txManager = context.getBean(PlatformTransactionManager.class);
		
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus ts = txManager.getTransaction(def);
		
		processor.process(file);
		ts.flush();
		txManager.commit(ts);
	}
	
	public static void main(String[] args) throws Exception {
		File file = new File(args[0]);
		DBProcessor.initDB(file);
	}
}
