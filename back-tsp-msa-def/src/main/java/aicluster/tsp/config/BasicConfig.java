package aicluster.tsp.config;

import aicluster.framework.interceptor.FwCoreInterceptor;
import bnet.library.bind.converter.*;
import bnet.library.bind.deserializer.*;
import bnet.library.bind.resolver.*;
import bnet.library.view.ExcelView;
import bnet.library.view.ReportExcelView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import kong.unirest.Unirest;
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
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

@Configuration
//@EnableWebMvc
@EnableScheduling
@EnableAspectJAutoProxy
//@EnableSwagger2
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

	@Bean
	public Docket sampleAPI() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.groupName("샘플")
				.select()
//				.apis(RequestHandlerSelectors.basePackage("aicluster.tsp.api.admin.dashboard"))
				.apis(RequestHandlerSelectors.any())					//Swagger API 문서로 만들기 원하는 basePackage 경로
				.paths(PathSelectors.ant("/api/sample/**"))
//				.paths(PathSelectors.any())								//URL 경로를 지정하여 해당 URL에 해당하는 요청만 SWAGGER로 만듦
				.build()
				.apiInfo(new ApiInfoBuilder()
						.title("실증포털 API ( 샘플 )")
						.description("샘플 api를 보여줍니다.")
						.version("1.0")
						.build());

	}

	@Bean
	public Docket dashboardAPI() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.groupName("대시보드")
				.select()
//				.apis(RequestHandlerSelectors.basePackage("aicluster.tsp.api.admin.dashboard"))
				.apis(RequestHandlerSelectors.any())					//Swagger API 문서로 만들기 원하는 basePackage 경로
				.paths(PathSelectors.ant("/api/admin/dashboard/**"))
//				.paths(PathSelectors.any())								//URL 경로를 지정하여 해당 URL에 해당하는 요청만 SWAGGER로 만듦
				.build()
				.apiInfo(new ApiInfoBuilder()
						.title("실증포털 API ( 대시보드 )")
						.description("대시보드 api를 보여줍니다.")
						.version("1.0")
						.build());
	}
	@Bean
	public Docket equipmentAPI() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.groupName("장비관리")
				.select()
//				.apis(RequestHandlerSelectors.basePackage("aicluster.tsp.api.admin.dashboard"))
				.apis(RequestHandlerSelectors.any())					//Swagger API 문서로 만들기 원하는 basePackage 경로
				.paths(PathSelectors.ant("/api/admin/eqpmns/**"))
//				.paths(PathSelectors.any())								//URL 경로를 지정하여 해당 URL에 해당하는 요청만 SWAGGER로 만듦
				.build()
				.apiInfo(new ApiInfoBuilder()
						.title("실증포털 API ( 장비관리 관련 )")
						.description("장비관리 api를 보여줍니다.")
						.version("1.0")
						.build());
	}
	@Bean
	public Docket equipmentInfoAPI() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.groupName("장비정보")
				.select()
//				.apis(RequestHandlerSelectors.basePackage("aicluster.tsp.api.admin.dashboard"))
				.apis(RequestHandlerSelectors.any())					//Swagger API 문서로 만들기 원하는 basePackage 경로
				.paths(PathSelectors.ant("/api/admin/eqpmns/info/**"))
//				.paths(PathSelectors.any())								//URL 경로를 지정하여 해당 URL에 해당하는 요청만 SWAGGER로 만듦
				.build()
				.apiInfo(new ApiInfoBuilder()
						.title("실증포털 API ( 장비정보 관련 )")
						.description("장비정보 api를 보여줍니다.")
						.version("1.0")
						.build());
	}
	@Bean
	public Docket equipmentCategoryAPI() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.groupName("장비분류")
				.select()
//				.apis(RequestHandlerSelectors.basePackage("aicluster.tsp.api.admin.dashboard"))
				.apis(RequestHandlerSelectors.any())					//Swagger API 문서로 만들기 원하는 basePackage 경로
				.paths(PathSelectors.ant("/api/admin/eqpmns/categories/**"))
//				.paths(PathSelectors.any())								//URL 경로를 지정하여 해당 URL에 해당하는 요청만 SWAGGER로 만듦
				.build()
				.apiInfo(new ApiInfoBuilder()
						.title("실증포털 API ( 장비정보 관련 )")
						.description("장비정보 api를 보여줍니다.")
						.version("1.0")
						.build());
	}
	@Bean
	public Docket dscntAPI() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.groupName("할인")
				.select()
//				.apis(RequestHandlerSelectors.basePackage("aicluster.tsp.api.admin.dashboard"))
				.apis(RequestHandlerSelectors.any())					//Swagger API 문서로 만들기 원하는 basePackage 경로
				.paths(PathSelectors.ant("/api/admin/eqpmns/dscnt/**"))
//				.paths(PathSelectors.any())								//URL 경로를 지정하여 해당 URL에 해당하는 요청만 SWAGGER로 만듦
				.build()
				.apiInfo(new ApiInfoBuilder()
						.title("실증포털 API ( 장비정보 관련 )")
						.description("장비정보 api를 보여줍니다.")
						.version("1.0")
						.build());
	}
	@Bean
	public Docket equipmentRentalAPI() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.groupName("장비 대여")
				.select()
//				.apis(RequestHandlerSelectors.basePackage("aicluster.tsp.api.admin.dashboard"))
				.apis(RequestHandlerSelectors.any())					//Swagger API 문서로 만들기 원하는 basePackage 경로
				.paths(PathSelectors.ant("/api/admin/eqpmns/rental/**"))
//				.paths(PathSelectors.any())								//URL 경로를 지정하여 해당 URL에 해당하는 요청만 SWAGGER로 만듦
				.build()
				.apiInfo(new ApiInfoBuilder()
						.title("실증포털 API ( 장비대여 관련 )")
						.description("장비대여 api를 보여줍니다.")
						.version("1.0")
						.build());
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.useDefaultResponseMessages(false)
				.select()
				//.apis(RequestHandlerSelectors.basePackage("aicluster.tsp.api.admin.dashboard"))
				.apis(RequestHandlerSelectors.any())					//Swagger API 문서로 만들기 원하는 basePackage 경로
				//.paths(PathSelectors.ant("/tsp/api/**"))
				.paths(PathSelectors.any())								//URL 경로를 지정하여 해당 URL에 해당하는 요청만 SWAGGER로 만듦
				.build()
				.apiInfo(new ApiInfoBuilder()
						.title("실증포털 API 전체")
						.description("실증포털 전체 api를 보여줍니다.")
						.version("1.0")
						.build());
	}
}
