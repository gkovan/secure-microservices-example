package com.ibm.microservice.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class JWTExpiredTokenException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String token;
	
	public JWTExpiredTokenException(String msg) {
		super(msg);
	}

    public JWTExpiredTokenException(String token, String msg, Throwable t) {
        super(msg, t);
        this.token = token;
    }
    
    public String token() {
        return this.token;
    }

}
