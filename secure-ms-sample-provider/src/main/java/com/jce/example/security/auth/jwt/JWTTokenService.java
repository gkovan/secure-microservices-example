package com.jce.example.security.auth.jwt;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import com.jce.example.security.config.JWTSettings;
import com.jce.example.security.exceptions.JWTExpiredTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JWTTokenService {
	private static final Logger logger = LoggerFactory.getLogger(JWTTokenService.class);
	
	private final JWTSettings jwtSettings;

	public final static List<String> DEFAULT_MS_SCOPES = Arrays.asList("ROLE_TRUSTED_CLIENT");
	public final static List<String> DEFAULT_MS_ACTUATOR_SCOPES = Arrays.asList("ROLE_TRUSTED_CLIENT", "ROLE_ACTUATOR");
	
	
	@Autowired
	public JWTTokenService(JWTSettings jwtSettings){
		this.jwtSettings = jwtSettings;
	}
	
	public String generateMSToken() {
		return generateToken(jwtSettings.getTokenIssuer(), DEFAULT_MS_SCOPES);
	}

	public String generateActuatorMSToken() {
		return generateToken(jwtSettings.getTokenIssuer(), DEFAULT_MS_ACTUATOR_SCOPES);
	}
	
	public String generateToken(String subject_user, List<String> scopes) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(Claims.SUBJECT, userDetails.getUsername());
//        claims.put(Claims.ISSUER, "the issuer");
//        claims.put(CLAIM_KEY_CREATED, new Date());
//        return generateToken(claims);
		
		
//        return Jwts.builder()
//                .setClaims(claims)
//                .setExpiration(generateExpirationDate())
//                .signWith(SignatureAlgorithm.HS512, secret)
//                .compact();
//      
		Claims claims = Jwts.claims().setSubject(subject_user);
		claims.put("scopes", scopes);
		
		DateTime currentTime = new DateTime();
				
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(jwtSettings.getTokenIssuer())
                .setIssuedAt(currentTime.toDate())
                .setExpiration(currentTime.plusMinutes(jwtSettings.getTokenExpirationTime()).toDate())
                //.signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                .signWith(SignatureAlgorithm.HS256, jwtSettings.getTokenSigningKey())
              .compact();
        return token;
	}
	
    public Jws<Claims> parseClaims(String token) {
    	String signingKey = jwtSettings.getTokenSigningKey();
    	logger.info("parseClaims:token={}, signingKey={}", token, signingKey);
    	
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException ex) {
            logger.error("Invalid JWT Token", ex);
            throw new BadCredentialsException("Invalid JWT token: ", ex);
        } catch (ExpiredJwtException expiredEx) {
            logger.info("JWT Token is expired", expiredEx);
            throw new JWTExpiredTokenException(token, "JWT Token expired", expiredEx);
        }
    }	

	
}
