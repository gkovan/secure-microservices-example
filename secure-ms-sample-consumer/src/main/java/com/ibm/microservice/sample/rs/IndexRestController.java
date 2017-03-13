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
public class IndexRestController {
	private static final Logger logger = LoggerFactory.getLogger(IndexRestController.class);
	
	private final RestTemplate restTemplate;
	
	@Autowired
	private JWTTokenService jwtTokenService;
	
	@Autowired
	private JWTSettings jwtSettings;
	
	@Value("${microservice.endpoint.ms1}")
	private String ms1Endpoint;

	@Value("${microservice.endpoint.ms2}")
	private String ms2Endpoint;
	
	public IndexRestController(){
		this.restTemplate = new RestTemplate();
	}
	
    @RequestMapping("/")
    public String index() {
    	logger.info("index called");
        return
                "<ul>" +
                "<li><a href=\"/token\">Test Token</a>" +
                   "<li><a href=\"/snoop\">Snoop</a>" +
                   "<li><a href=\"/exception\">Exception</a>" +
                "</ul>";
    }

    @RequestMapping("/token")
    public ResponseEntity<String> testToken() {
    	logger.info("testToken called");
    	
		String token = jwtTokenService.generateMSToken();
		
		logger.info("#$#$# token={}", token);
		return ResponseEntity.ok(token);  	
     }

    @RequestMapping("/snoop")
	public ResponseEntity<String> snoopclient(){
		logger.info("snoopclient called");
		String token = jwtTokenService.generateMSToken();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(jwtSettings.getTokenHeaderParam(), "Bearer "+token);
		
		String url = ms1Endpoint+"/api/snoop";
		logger.info("url={}", url);
		
		ResponseEntity<String> entity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
		
		logger.debug(entity.getBody());
				
		return ResponseEntity.ok(entity.getBody());
	}
    
    @RequestMapping("/exception")
    public ResponseEntity<String> exceptionClient() {
    	logger.info("exception called");
		String token = jwtTokenService.generateMSToken();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(jwtSettings.getTokenHeaderParam(), "Bearer "+token);
		
		String url = ms2Endpoint+"/api/exception";
		logger.info("url={}", url);
		
		try {
			ResponseEntity<String> entity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
			
			logger.debug(entity.getBody());
			return ResponseEntity.ok(entity.getBody());
		} catch (Exception e) {
			logger.error("exceptionClient problem:", e);
			return ResponseEntity.ok("exceptionClient problem: "+e.getMessage());
		}
    }
}
