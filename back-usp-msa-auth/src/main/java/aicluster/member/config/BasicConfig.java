package aicluster.member.config;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import aicluster.framework.interceptor.FwCoreInterceptor;
import bnet.library.bind.converter.BigDecimalConverter;
import bnet.library.bind.converter.BooleanConverter;
import bnet.library.bind.converter.DateConverter;
import bnet.library.bind.converter.DoubleConverter;
import bnet.library.bind.converter.FloatConverter;
import bnet.library.bind.converter.IntegerConverter;
import bnet.library.bind.converter.LongConverter;
import bnet.library.bind.converter.ShortConverter;
import bnet.library.bind.deserializer.BigDecimalDeserializer;
import bnet.library.bind.deserializer.BooleanDeserializer;
import bnet.library.bind.deserializer.DateDeserializer;
import bnet.library.bind.deserializer.DoubleDeserializer;
import bnet.library.bind.deserializer.FloatDeserializer;
import bnet.library.bind.deserializer.IntegerDeserializer;
import bnet.library.bind.deserializer.LongDeserializer;
import bnet.library.bind.deserializer.ShortDeserializer;
import bnet.library.bind.resolver.BigDecimalArgumentResolver;
import bnet.library.bind.resolver.BooleanArgumentResolver;
import bnet.library.bind.resolver.DateArgumentResolver;
import bnet.library.bind.resolver.DoubleArgumentResolver;
import bnet.library.bind.resolver.FloatArgumentResolver;
import bnet.library.bind.resolver.IntegerArgumentResolver;
import bnet.library.bind.resolver.LongArgumentResolver;
import bnet.library.bind.resolver.ShortArgumentResolver;
import bnet.library.view.ExcelView;
import bnet.library.view.ReportExcelView;
import kong.unirest.Unirest;

@Configuration
//@EnableWebMvc
@EnableScheduling
@EnableAspectJAutoProxy
public class BasicConfig implements SchedulingConfigurer, WebMvcConfigurer {

	@Autowired
	protected FwCoreInterceptor interceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor)
				.addPathPatterns("/**");
//				.excludePathPatterns("/component-assets/**");
//		registry.addInterceptor(localeChangeInterceptor())
//				.addPathPatterns("/**");
	}

	@Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(10); // 조정해 주세요.
        taskScheduler.initialize();
        taskRegistrar.setTaskScheduler(taskScheduler);
    }

	@Bean
	public ViewResolver viewResolver() {
		BeanNameViewResolver viewResolver = new BeanNameViewResolver();
		viewResolver.setOrder(1);
		return viewResolver;
	}

	@Bean("excelView")
	public ExcelView excelView() {
		return new ExcelView();
	}

	@Bean("reportExcelView")
	public ReportExcelView reportExcelView() {
		return new ReportExcelView();
	}

	@Bean("jsonView")
	public MappingJackson2JsonView jsonView() {
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView();
		jsonView.setExtractValueFromSingleKeyModel(true);
		jsonView.setPrettyPrint(true);
		return jsonView;
	}

	@PostConstruct
	public void postConstruct() {
		ObjectMapper mapper = thenetObjectMapper();
		Unirest.config().setObjectMapper(new kong.unirest.ObjectMapper() {
			@Override
			public <T> T readValue(String value, Class<T> valueType) {
				try {
                    return mapper.readValue(value, valueType);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
			}

			@Override
			public String writeValue(Object value) {
				try {
					return mapper.writeValueAsString(value);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	//@Bean
    public ObjectMapper thenetObjectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

        SimpleModule module = new SimpleModule("ThenetDeserializer");

        module.addDeserializer(Date.class, new DateDeserializer());
        module.addDeserializer(Boolean.class, new BooleanDeserializer());
        module.addDeserializer(BigDecimal.class, new BigDecimalDeserializer());
        module.addDeserializer(Double.class, new DoubleDeserializer());
        module.addDeserializer(Float.class, new FloatDeserializer());
        module.addDeserializer(Integer.class, new IntegerDeserializer());
        module.addDeserializer(Long.class, new LongDeserializer());
        module.addDeserializer(Short.class, new ShortDeserializer());

        objectMapper.registerModule(module);

        return objectMapper;
    }


	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new BooleanConverter());
		registry.addConverter(new BigDecimalConverter());
		registry.addConverter(new DateConverter());
		registry.addConverter(new DoubleConverter());
		registry.addConverter(new FloatConverter());
		registry.addConverter(new IntegerConverter());
		registry.addConverter(new LongConverter());
		registry.addConverter(new ShortConverter());
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new BigDecimalArgumentResolver());
		argumentResolvers.add(new BooleanArgumentResolver());
		argumentResolvers.add(new DateArgumentResolver());
		argumentResolvers.add(new DoubleArgumentResolver());
		argumentResolvers.add(new FloatArgumentResolver());
		argumentResolvers.add(new IntegerArgumentResolver());
		argumentResolvers.add(new LongArgumentResolver());
		argumentResolvers.add(new ShortArgumentResolver());
	}

	@Bean
	public HttpMessageConverter<?> jsonConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(thenetObjectMapper());
		converter.setDefaultCharset(Charset.forName("UTF-8"));

		return converter;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(jsonConverter());
	}
}
