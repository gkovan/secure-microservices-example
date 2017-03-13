package com.ibm.microservice.sample;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ibm.microservice.security.auth.jwt.JWTAuthenticationProcessingFilter;
import com.ibm.microservice.security.auth.jwt.SkipPathRequestMatcher;
import com.ibm.microservice.security.config.BaseWebSecurityConfig;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends BaseWebSecurityConfig {

	private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	
    //public static final String TOKEN_AUTH_ENTRY_POINT = "/api/**";
	public static final String TOKEN_AUTH_ENTRY_POINT = "/**";

 	
//    @Bean
//    public FilterRegistrationBean corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.addAllowedOrigin("http://domain1.com");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        source.registerCorsConfiguration("/**", config);
//        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
//        bean.setOrder(0);
//        return bean;
//    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST","OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		configureHTTPDefault(http);
                
        http
        .authorizeRequests()
//        .antMatchers(
//                HttpMethod.GET,
//                "/test"
//        ).permitAll()
        .antMatchers(getDefaultSkipList().toArray(new String[0])).permitAll()
        .and()
        .addFilterBefore(buildJWTTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        
        
        logger.info("configure:after HttpSecurity={}", http);
        //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	}
	
	@Bean
	@Override
	protected JWTAuthenticationProcessingFilter buildJWTTokenAuthenticationProcessingFilter() throws Exception {
		return super.buildJWTTokenAuthenticationProcessingFilter();
	}
    
    @Override
	protected SkipPathRequestMatcher getSkipPathRequestMatcher() {
		List<String> pathsToSkip = getDefaultSkipList();
		
		return new SkipPathRequestMatcher(pathsToSkip, TOKEN_AUTH_ENTRY_POINT);
	}
}
