package dev.jinulab.isetdx.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

//2026-02-24 L.JW: @Configuration 살려놓으면 Swagger 안됨
@Configuration
public class CorsConfig {
	
	private static final String[] ALLOWED_METHODS = new String[] { "OPTIONS", "HEAD", "GET", "PUT", "PATCH", "POST", "DELETE" };
	private static final String MAPPING_CORS = "/iam/**";
	
	@Value("${jinulab.iam.api-path-patterns:/v1/**,/frm/**,/pns/**}")
    private String[] apiPathPatterns;

    @Value("${server.servlet.context-path:}")
    private String contextPath;
    
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean() {
        
        CorsConfiguration config = new CorsConfiguration();
//        config.applyPermitDefaultValues();
//        config.setAllowCredentials(true);
//        config.setAllowedOrigins(Arrays.asList("*"));
//        config.setAllowedHeaders(Arrays.asList("*"));
//        config.setAllowedMethods(Arrays.asList(ALLOWED_METHODS));
//        config.setExposedHeaders(Arrays.asList("content-length"));
        config.applyPermitDefaultValues();
        config.setAllowCredentials(false);
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList(ALLOWED_METHODS));
        config.setExposedHeaders(Arrays.asList("content-length"));
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //source.registerCorsConfiguration(MAPPING_CORS, config);
        source.registerCorsConfiguration("/**", config);
        for (String path : apiPathPatterns) {
            source.registerCorsConfiguration(path, config);
        }
        
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

}
