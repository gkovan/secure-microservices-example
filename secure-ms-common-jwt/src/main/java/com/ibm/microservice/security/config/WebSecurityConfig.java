package com.ibm.microservice.security.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ibm.microservice.security.auth.jwt.JWTAuthenticationProcessingFilter;
import com.ibm.microservice.security.auth.jwt.JWTAuthenticationProvider;
import com.ibm.microservice.security.auth.jwt.SkipPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	
    //public static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";
    
    //public static final String TOKEN_AUTH_ENTRY_POINT = "/api/**";
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
    
    @Bean
    protected JWTAuthenticationProcessingFilter buildJWTTokenAuthenticationProcessingFilter() throws Exception {
    	logger.info("buildJwtTokenAuthenticationProcessingFilter:called");
    	
    	List<String> pathsToSkip = getSwaggerSkipList();
    	
     	logger.info("buildJwtTokenAuthenticationProcessingFilter: TOKEN_AUTH_ENTRY_POINT={}", TOKEN_AUTH_ENTRY_POINT);
     	logger.info("buildJwtTokenAuthenticationProcessingFilter: pathsToSkip={}", pathsToSkip);
 
     	SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, TOKEN_AUTH_ENTRY_POINT);
        JWTAuthenticationProcessingFilter filter = new JWTAuthenticationProcessingFilter(matcher);
        
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		logger.info("configure:before HttpSecurity={}", http);
		
		// disable caching
        http.headers().cacheControl();
                
        http
        // Do not need CSRF for JWT based authentication
        .csrf().disable()
        .cors().and()
        // Do not create session
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
       
        .authorizeRequests()
//        .antMatchers(
//                HttpMethod.GET,
//                "/test"
//        ).permitAll()
        .antMatchers(getSwaggerSkipList().toArray(new String[0])).permitAll()
        .and()
        .addFilterBefore(buildJWTTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        
        
        
        logger.info("configure:after HttpSecurity={}", http);
        //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	}
	
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
    	logger.info("authenticationManagerBean:called");
        return super.authenticationManagerBean();
    }
    
    protected void configure(AuthenticationManagerBuilder auth) {
    	logger.info("configure: AuthenticationManagerBuilder={}", auth);
        auth.authenticationProvider(jwtAuthenticationProvider);
    }
    
    private List<String> getSwaggerSkipList(){
    	
//     	String skipServiceInfo = cxf_path + cxf_service_list_path+"/**";
//     	String skipServiceApiDocs = cxf_path + "/api-docs/**";
//     	String skipSwaggerJson = cxf_path + "/swagger.json";
//     	String skipSwaggerYaml = cxf_path + "/swagger.yaml";
//     	String skipSnoop = cxf_path + "/snoop";
//     	
     	List<String> pathsToSkip = new ArrayList<String>();
//     	pathsToSkip.add(skipServiceInfo);
//     	pathsToSkip.add(skipServiceApiDocs);
//     	pathsToSkip.add(skipSwaggerJson);
//     	pathsToSkip.add(skipSwaggerYaml);
//     	pathsToSkip.add(skipSnoop);
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

     	
    	//List<String> pathsToSkip = Arrays.asList(skipServiceInfo, skipServiceApiDocs, skipSwaggerJson);
    	
    	
     	logger.info("getSwaggerSkipList: pathsToSkip={}", pathsToSkip);
     	return pathsToSkip;
    }
	
}
