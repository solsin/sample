package config;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.lifecycle.ResourceProvider;
import org.apache.cxf.jaxrs.spring.SpringResourceFactory;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.LocaleResolver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

import lombok.extern.slf4j.Slf4j;
import sample.exception.RestfulExceptionMapper;


/**
 * Configuration for CXF Server and Client
 * 
 * @author charles <charles@plgrim.com>
 *
 */
@Configuration
@Slf4j
public class CXFConfig {

	@Autowired
	private ApplicationContext ctx;

	@Autowired
	private LocaleResolver localeResolver;

	@Bean
	@JsonInclude
	public JacksonJsonProvider jsonProvider() {
		ObjectMapper mapper = new ObjectMapper();
		JacksonJsonProvider jackson = new JacksonJsonProvider(mapper);
		return jackson;
	}
	
	@Bean
	public SpringBus cxf() {
		SpringBus bus = new SpringBus();
		bus.setApplicationContext(ctx);
		return bus;
	}

	@Bean
	public JAXRSServerFactoryBean jaxRsServerFactory(JacksonJsonProvider jsonProvider, SpringBus bus) {
		log.info("CXFConfig start initialzing...");
		LinkedList<ResourceProvider> resourceProviders = new LinkedList<>();

		// auto scan cxf api component
		for (String beanName : ctx.getBeanDefinitionNames()) {
			if (ctx.findAnnotationOnBean(beanName, Path.class) == null) {
				if (log.isTraceEnabled()) {
					log.trace("not CXF bean:{} skiped", beanName);
				}
				continue;
			}

			SpringResourceFactory factory = new SpringResourceFactory(beanName);
			factory.setApplicationContext(ctx);
			resourceProviders.add(factory);
			log.info("CXF bean:{} added to resource provider", beanName);
		}

		JAXRSServerFactoryBean factory = new JAXRSServerFactoryBean();
		factory.setBus(bus);
		List<Object> providers = new LinkedList<>();
		providers.add(jsonProvider);

		providers.add(new RestfulExceptionMapper());

		factory.setProviders(providers);
		factory.setResourceProviders(resourceProviders);

		List<Interceptor<? extends Message>> interceptors = new LinkedList<>();
		interceptors.add(new CXFLocaleInterceptor(localeResolver));
		factory.setInInterceptors(interceptors);

		// if (swaggerEnabled) {
		// List<Feature> features = new LinkedList<>();
		// features.add(swagger2Feature());
		// factory.setFeatures(features);
		// }

		factory.setAddress("/");

		if (log.isDebugEnabled()) {
			log.debug("cxf ServiceFactory: {}", factory.getServiceFactory());
		}

		log.info("CXFConfig initialzed");
		return factory;
	}
	
    @Bean
    public Server jaxRsServer(JAXRSServerFactoryBean factory) {
        return factory.create();
    }
    
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Locale interceptor for CXF api
	 */
	public class CXFLocaleInterceptor extends AbstractPhaseInterceptor<Message> {
		LocaleResolver localeResolver;

		/**
		 * default constructor : set phase to Pahse.RECEIVE
		 */
		public CXFLocaleInterceptor(LocaleResolver localeResolver) {
			super(Phase.RECEIVE);
			this.localeResolver = localeResolver;
		}

		/**
		 * message handler
		 * 
		 * @see org.apache.cxf.interceptor.Interceptor#handleMessage(org.apache.cxf.message.Message)
		 * @param message
		 *            cxf jaxrs message
		 */
		@Override
		public void handleMessage(Message message) {
			if (localeResolver == null) {
				log.warn("No LocaleResolver found: not in a DispatcherServlet request?");
				return;
			}

			HttpServletRequest httpRequest = (HttpServletRequest) message.get("HTTP.REQUEST");
			Locale locale = localeResolver.resolveLocale(httpRequest);
			if (locale == null) {
				locale = Locale.getDefault();
			}
			LocaleContextHolder.setLocale(locale);
		}
	}
}