package com.ibm.microservice.sample.security.auth.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.ibm.microservice.sample.security.config.JWTSettings;

public class JWTAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
	private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationProcessingFilter.class);
	
	public static final String HEADER_PREFIX = "Bearer ";
	
	@Autowired JWTSettings jwtSettings;
	
    public JWTAuthenticationProcessingFilter(RequestMatcher matcher) {
        super(matcher);
        logger.info("<init> matcher={}",matcher);
    }
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		String tokenPayload = request.getHeader(jwtSettings.getTokenHeaderParam());
		
		logger.info("attemptAuthentication: tokenPayload={}", tokenPayload);
		String tokenExtr = extract(tokenPayload);
		
		logger.info("attemptAuthentication: after extract tokenExtr={}", tokenExtr);
		
		return getAuthenticationManager().authenticate(new JWTAuthenticationToken(tokenExtr));
	}

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        //TODO: is overriding really required
    	logger.info("successfulAuthentication: authResult={}", authResult);
    	
    	SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }
	
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        //SecurityContextHolder.clearContext();
    	logger.info("unsuccessfulAuthentication: failed={}", failed);
    	
        super.unsuccessfulAuthentication(request, response, failed);
        //failureHandler.onAuthenticationFailure(request, response, failed);
    }
    
	
	protected String extract(String header) {
		logger.info("extract: header={}", header);
        if (StringUtils.isBlank(header)) {
            throw new AuthenticationServiceException("Authorization header is blank.");
        }

        if (header.length() < HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid Authorization header size.");
        }

        return header.substring(HEADER_PREFIX.length(), header.length());
	}
}
