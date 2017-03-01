package com.ibm.microservice.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "demo.security.jwt", ignoreUnknownFields = true)
public class JWTSettings {
	
	private String tokenHeaderParam = "X-Authorization";
	
    /**
     * Token will expire after this time.
     */
    private Integer tokenExpirationTime;

    /**
     * Token issuer.
     */
    private String tokenIssuer;
    
    /**
     * Key is used to sign Token.
     */
    private String tokenSigningKey;
    
    public final Integer getTokenExpirationTime() {
		return tokenExpirationTime;
	}

	public void setTokenExpirationTime(Integer tokenExpirationTime) {
		this.tokenExpirationTime = tokenExpirationTime;
	}

	public final String getTokenIssuer() {
		return tokenIssuer;
	}

	public void setTokenIssuer(String tokenIssuer) {
		this.tokenIssuer = tokenIssuer;
	}

	public final String getTokenSigningKey() {
		return tokenSigningKey;
	}

	public void setTokenSigningKey(String tokenSigningKey) {
		this.tokenSigningKey = tokenSigningKey;
	}

	public final String getTokenHeaderParam() {
		return tokenHeaderParam;
	}

	public void setTokenHeaderParam(String tokenHeaderParam) {
		this.tokenHeaderParam = tokenHeaderParam;
	}

	
}
