package dev.jinulab.isetdx.config;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jackson.autoconfigure.JsonMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.jinulab.framework.config.properties.EntryProperties;
import dev.jinulab.framework.config.properties.ErrorProperties;
import dev.jinulab.framework.config.properties.FileServiceProperties;
import dev.jinulab.framework.config.properties.MsaProperties;
import dev.jinulab.framework.config.properties.SecurityProperties;
import dev.jinulab.framework.config.properties.ServiceProperties;
import dev.jinulab.framework.util.BeanUtils;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;

@Configuration
@EnableConfigurationProperties({ ServiceProperties.class, MsaProperties.class, EntryProperties.class,
				ErrorProperties.class, SecurityProperties.class,
				FileServiceProperties.class })
public class AppConfig {
	
	private static final String dateFormat = "yyyy-MM-dd";
    private static final String datetimeFormat = "yyyy-MM-dd'T'HH:mm:ssZ";

    //Parameter 0 of method setCacheManager 에러 발생해서 주석 처리함
//  @Autowired
//	public void setCacheManager(CacheManager cacheManager) {
//		CacheUtils.setCacheManager(cacheManager);
//	}
    
    @Value("${server.timezone:Asia/Seoul}")
    private String timezone;
    
    @Bean
    JsonMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        
        TimeZone.setDefault(TimeZone.getTimeZone(timezone));
        
//        return jacksonObjectMapperBuilder -> {
//            jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
//            jacksonObjectMapperBuilder.simpleDateFormat(datetimeFormat);
//            jacksonObjectMapperBuilder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)));
//            jacksonObjectMapperBuilder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(datetimeFormat)));
//        };
        return builder -> builder
                // ✅ timeZone() → defaultTimeZone()
                .defaultTimeZone(TimeZone.getTimeZone(timezone))
                // ✅ simpleDateFormat() → defaultDateFormat()
                .defaultDateFormat(new SimpleDateFormat(datetimeFormat))
                // ✅ serializers() → addSerializer() (모듈로 등록)
                .addModule(new tools.jackson.databind.module.SimpleModule()
                    .addSerializer(LocalDate.class,
                        new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormat)))
                    .addSerializer(LocalDateTime.class,
                        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(datetimeFormat)))
                );
    }
    
    @Bean
	BeanUtils beanUtils() {
		return new BeanUtils();
	}

    //이게 있으면 필터를 수행하는것 같아서 일단 주석 처리해보고 진행해보자
//	@Bean
//	public FilterRegistrationBean<EntryFilter> entryFilter() {
//		EntryFilter entryFilter = new EntryFilter();
//		FilterRegistrationBean<EntryFilter> bean = new FilterRegistrationBean<EntryFilter>(entryFilter);
//		bean.addUrlPatterns("/*");
//		bean.setOrder(1);
//		return bean;
//	}

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
//	@Bean
//	PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}
}