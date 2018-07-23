package sample.config;

import javax.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = {"sample", "processor"})
@EnableJpaRepositories(basePackages = {"sample.repositories"}, entityManagerFactoryRef="entityManagerFactoryBean")
@Import({JPAConfig.class})
@PropertySource("classpath:application.test.properties")
@Slf4j
public class TestConfig {

	@PostConstruct
	public void init() {
		log.info("call TestConfig.init");
	}
}