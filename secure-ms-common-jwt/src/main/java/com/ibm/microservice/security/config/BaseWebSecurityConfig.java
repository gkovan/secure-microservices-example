package com.ibm.microservice.security.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.ibm.microservice.security.auth.jwt.JWTAuthenticationProcessingFilter;
import com.ibm.microservice.security.auth.jwt.JWTAuthenticationProvider;
import com.ibm.microservice.security.auth.jwt.SkipPathRequestMatcher;



public abstract class BaseWebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(BaseWebSecurityConfig.class);
	
	public static final String TOKEN_AUTH_ENTRY_POINT = "/**";

    @Autowired private JWTAuthenticationProvider jwtAuthenticationProvider;
    @Autowired private AuthenticationManager authenticationManager;
	
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

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST","OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
    
    
    protected abstract SkipPathRequestMatcher getSkipPathRequestMatcher();
    
    
    protected JWTAuthenticationProcessingFilter buildJWTTokenAuthenticationProcessingFilter() throws Exception {
    	logger.info("buildJwtTokenAuthenticationProcessingFilter:called");
    	
     	SkipPathRequestMatcher matcher = getSkipPathRequestMatcher();
     	logger.info("buildJwtTokenAuthenticationProcessingFilter: matcher={}", matcher);
     	
     	JWTAuthenticationProcessingFilter filter = new JWTAuthenticationProcessingFilter(matcher);
        
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }
	
    protected void configureHTTPDefault(HttpSecurity http) throws Exception{
		logger.info("configure:before HttpSecurity={}", http);
		
		// disable caching
        http.headers().cacheControl();
                
        http
        // Do not need CSRF for JWT based authentication
        .csrf().disable()
        .cors().and()
        // Do not create session
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }
    
	
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
    	logger.info("authenticationManagerBean:called");
        return super.authenticationManagerBean();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
    	logger.info("configure: AuthenticationManagerBuilder={}", auth);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }
    
    protected List<String> getDefaultSkipList(){
    	     	
     	List<String> pathsToSkip = new ArrayList<String>();

     	pathsToSkip.add("/actuator/health");
     	pathsToSkip.add("/api/token");
     	pathsToSkip.add("/api/actuatortoken");
     	pathsToSkip.add("/api-docs/**");
     	pathsToSkip.add("/swagger/**");
     	pathsToSkip.add("/swagger-resources/**");
     	
     	
     	pathsToSkip.add("/*.html");
     	pathsToSkip.add("/favicon.ico");
     	pathsToSkip.add("/**/*.html");
     	pathsToSkip.add("/**/*.css");
     	pathsToSkip.add("/**/*.js");
     	pathsToSkip.add("/**/*.png");
     	pathsToSkip.add("/**/*.ttf");
     	pathsToSkip.add("/**/*.gif");

     	
     	logger.info("getSwaggerSkipList: pathsToSkip={}", pathsToSkip);
     	return pathsToSkip;
    }
}
