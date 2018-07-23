package sample.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableTransactionManagement
@Slf4j
public class JPAConfig {
	@Value("${jpa.hibernate.dialect}")
	String jpaHibernateDialect;
	
	@Value("${jpa.datasoruce.driver.class.name}")
	String jpaDatasourceDriverClassName;

	@Value("${jpa.datasource.url}")
	String jpaDatasourceUrl;
	
	@Value("${jpa.datasource.username}")
	String jpaDatasourceUsername;
	
	@Value("${jpa.datasource.password}")
	String jpaDatasourcePassword;
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(DataSource ds) {
		LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactory.setDataSource(ds);
		entityManagerFactory.setPackagesToScan("sample.entities");

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		entityManagerFactory.setJpaVendorAdapter(vendorAdapter);
		entityManagerFactory.setJpaProperties(additionalProperties());

		log.info("finish setup entityManagerFactoryBean");

		return entityManagerFactory;
	}

	Properties additionalProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		properties.setProperty("hibernate.dialect", jpaHibernateDialect);

		return properties;
	}

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(jpaDatasourceDriverClassName);
        dataSource.setUrl(jpaDatasourceUrl);
        dataSource.setUsername(jpaDatasourceUsername);
        dataSource.setPassword(jpaDatasourcePassword);
		return dataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);

		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
}
