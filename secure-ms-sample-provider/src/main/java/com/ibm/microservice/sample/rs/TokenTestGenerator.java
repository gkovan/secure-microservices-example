package com.ibm.microservice.sample.rs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.microservice.security.auth.jwt.JWTTokenService;

@RestController
public class TokenTestGenerator {
	private static final Logger logger = LoggerFactory.getLogger(TokenTestGenerator.class);
	
	@Autowired
	private JWTTokenService jwtTokenService;
	
	@RequestMapping("/api/token")
	//@ResponseBody
	public ResponseEntity<String> jwtTestToken()  {
		
		String token = jwtTokenService.generateMSToken();
		
		logger.info("#$#$# token={}", token);
		
		//return  new  ResponseEntity<String>(token, HttpStatus.OK);
		return ResponseEntity.ok(token);
	}	

	@RequestMapping("/api/actuatortoken")
	//@ResponseBody
	public ResponseEntity<String> jwtActuatorTestToken()  {
		
		String token = jwtTokenService.generateActuatorMSToken();
		
		//return  new  ResponseEntity<String>(token, HttpStatus.OK);
		return ResponseEntity.ok(token);
	}
}
