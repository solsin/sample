package config;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.MDC;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

/**
 * Servlet filter µî·Ï for Spring Boot
 * 
 * @author charles <charles@plgrim.com>
 *
 */
@Configuration
@Slf4j
public class FilterConfig {
	@Bean
	public FilterRegistrationBean<Filter> mdcFilterRegistration() {

	    FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
	    registration.setFilter(mdcFilter());
	    registration.addUrlPatterns("/*");
//	    registration.addInitParameter("paramName", "paramValue");
	    registration.setName("mdcFilter");
	    registration.setOrder(1);
	    
	    log.info("registed MDCFilter for /*");
	    return registration;
	} 

	public Filter mdcFilter() {
	    return new MDCFilter();
	}
	
	public static class MDCFilter implements Filter {
		public static String EXECUTION_ID = "EXECUTION_ID";
		
		@Override
		public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
			log.info("called init: do nothing");
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
				throws IOException, ServletException {
			
			MDC.put(EXECUTION_ID, RandomStringUtils.random(15));
			chain.doFilter(request, response);
		}

		@Override
		public void destroy() {
			log.info("called destroy: do nothing");
		}
		
	}
}
