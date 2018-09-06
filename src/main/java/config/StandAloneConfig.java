package config;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Spring Boot를 사용하지 않고, spring container를 stand alone application으로 사용하고 싶을 경우 사용
 * 
 * @author Chulhui Park <charles@plgrim.com>
 *
 */
@Configuration
@ComponentScan(basePackages = {"processor"})
@EnableJpaRepositories(basePackages = {"sample.repositories"}, entityManagerFactoryRef="entityManagerFactoryBean")
@MapperScan("sample.repositories")
public class StandAloneConfig {
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
	    return new PropertySourcesPlaceholderConfigurer();
	}
	
    @Bean
    public SqlSessionFactoryBean sqlSessionFactory(DataSource ds) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(ds);
        return sessionFactory;
    }
}
