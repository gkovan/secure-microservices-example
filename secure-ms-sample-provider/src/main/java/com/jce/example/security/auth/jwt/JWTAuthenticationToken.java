package com.jce.example.security.auth.jwt;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.jce.example.security.model.UserContext;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {
	private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationToken.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private String rawAccessToken;
    private UserContext userContext;

    
    public JWTAuthenticationToken(UserContext userContext, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		logger.info("<init>: userContext={}, authorities={}",userContext,authorities);
        this.eraseCredentials();
        this.userContext = userContext;
        super.setAuthenticated(true);
	}

    public JWTAuthenticationToken(String unsafeToken) {
        super(null);
        this.rawAccessToken = unsafeToken;
        logger.info("<init>: unsafeToken={}",unsafeToken);
        this.setAuthenticated(false);
    }
	
	@Override
	public Object getCredentials() {
		//logger.info("getCredentials");
		return this.rawAccessToken;
	}

	@Override
	public Object getPrincipal() {
		//logger.info("getPrincipal");
		return this.userContext;
	}

    @Override
    public void setAuthenticated(boolean authenticated) {
    	//logger.info("setAuthenticated:authenticated={}",authenticated);
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }
    
    @Override
    public void eraseCredentials() {
    	//logger.info("eraseCredentials");
        super.eraseCredentials();
        this.rawAccessToken = null;
    }
}
