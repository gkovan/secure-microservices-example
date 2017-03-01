package com.ibm.microservice.sample.rs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ibm.microservice.security.auth.jwt.JWTTokenService;
import com.ibm.microservice.security.config.JWTSettings;

@RestController
public class SnoopClientResource {
	private static final Logger logger = LoggerFactory.getLogger(SnoopClientResource.class);
	private final RestTemplate restTemplate;
	
	@Autowired
	private JWTTokenService jwtTokenService;
	
	@Autowired
	private JWTSettings jwtSettings;
	
	@Value("${microservice.endpoint.snoop}")
	private String snoopEndpoint;

	
	public SnoopClientResource() {
		
		this.restTemplate = new RestTemplate();
	}
	
	@RequestMapping("/")
	private ResponseEntity<String> snoopclient(){
		String token = jwtTokenService.generateMSToken();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(jwtSettings.getTokenHeaderParam(), "Bearer "+token);
		
		String url = snoopEndpoint+"/api/snoop";
		logger.info("url={}", url);
		
		ResponseEntity<String> entity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
		
		
		//assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		//assertThat(entity.getBody()).isNotEmpty();
		
		
		System.out.println(entity.getBody());
		
		return ResponseEntity.ok(entity.getBody());
	}
}
