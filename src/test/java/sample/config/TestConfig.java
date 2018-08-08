package sample.config;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
		WebMvcAutoConfiguration.class, EmbeddedWebServerFactoryCustomizerAutoConfiguration.class})
@SpringBootApplication
@ComponentScan(basePackages = { "none" }, 
	excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JPAConfig.class))
@PropertySource({ "classpath:application.general.test.properties" })
@Slf4j
public class TestConfig {

	@PostConstruct
	public void init() {
		log.info("call TestConfig.init");
	}
}