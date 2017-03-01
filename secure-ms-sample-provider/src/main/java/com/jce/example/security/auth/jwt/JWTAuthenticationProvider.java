package com.jce.example.security.auth.jwt;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.jce.example.security.model.UserContext;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {

	private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationProvider.class);
	
	private final JWTTokenService jwtTokenService;
		
	@Autowired
	public JWTAuthenticationProvider(JWTTokenService jwtTokenService) {
		logger.info("<init> jwtTokenService={}", jwtTokenService);
		this.jwtTokenService = jwtTokenService;
	}

	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		logger.info("authenticate: authentication={}", authentication);
		
		String rawAccessToken = (String) authentication.getCredentials();
		logger.info("authenticate: rawAccessToken={}", rawAccessToken);
		
        Jws<Claims> jwsClaims = jwtTokenService.parseClaims(rawAccessToken);
        logger.info("authenticate: after parseClaims jwsClaims={}", jwsClaims);
        
        String subject = jwsClaims.getBody().getSubject();
        logger.info("authenticate: claim subject={}", subject);
        
        //TODO: scope/roles validation
		@SuppressWarnings("unchecked")
		List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
        
		List<GrantedAuthority> authorities = scopes.stream()
                .map(authority -> new SimpleGrantedAuthority(authority))
                .collect(Collectors.toList());
        
        //List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();                
        //authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        //authorities.add(new SimpleGrantedAuthority("ROLE_ACTUATOR"));
        
        
        //List<GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;
        
        logger.info("authenticate: create  authorities={} ", authorities);
        
        UserContext context = UserContext.create(subject, authorities);
        logger.info("authenticate: create  UserContext={} and returning JWTAuthenticationToken", context);
        
        return new JWTAuthenticationToken(context, context.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		logger.info("supports: authentication={} ", authentication);
		
		return (JWTAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
