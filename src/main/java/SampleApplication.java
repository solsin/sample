import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import config.WebMVCConfig;
import lombok.extern.slf4j.Slf4j;
import sample.config.JPAConfig;

/**
 * Spring Boot application starter
 * @author solsi
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"sample", "processor"})
@EnableJpaRepositories(basePackages = {"sample.repositories"}, entityManagerFactoryRef="entityManagerFactoryBean")
@MapperScan("sample.repositories")
@Import({JPAConfig.class, WebMVCConfig.class})
@PropertySource({"classpath:application.properties"})
@Slf4j
public class SampleApplication {

	public static void main(String[] args) {
		log.info("start sample project");
		SpringApplication.run(SampleApplication.class, args);
	}

}
