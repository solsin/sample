import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import lombok.extern.slf4j.Slf4j;

/**
 * Spring Boot application starter
 * @author solsi
 *
 */
@SpringBootApplication
@ComponentScan("sample")
@EnableJpaRepositories(basePackages = "sample.repo")
@Slf4j
public class Application {

	public static void main(String[] args) {
		log.info("start sample project");
		SpringApplication.run(Application.class, args);
	}

}