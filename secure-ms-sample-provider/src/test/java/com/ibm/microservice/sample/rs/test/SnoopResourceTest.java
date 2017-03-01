package com.ibm.microservice.sample.rs.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.ibm.microservice.sample.security.auth.jwt.JWTTokenService;
import com.ibm.microservice.sample.security.config.JWTSettings;


//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = DemoSecureApplication.class)
//@AutoConfigureMockMvc
//@WebAppConfiguration

//@RunWith(SpringRunner.class)
//@WebMvcTest(TokenTestGenerator.class)
//@AutoConfigureMockMvc(secure=false)


@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("development")
public class SnoopResourceTest {

	@Autowired
	private TestRestTemplate template;

	@Autowired
	private JWTTokenService jwtTokenService;
	
	@Autowired
	private JWTSettings jwtSettings;
	

	@Test
	public void testActuator() throws Exception {

		String token = jwtTokenService.generateActuatorMSToken();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(jwtSettings.getTokenHeaderParam(), "Bearer "+token);
		
		ResponseEntity<String> entity = template.exchange("/actuator/auditevents", HttpMethod.GET, new HttpEntity<>(headers), String.class);
		
		
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody()).isNotEmpty();
		
		System.out.println(entity.getBody());
	}
	
	@Test
	public void testActuatorWithoutAuthority() throws Exception {

		String token = jwtTokenService.generateMSToken();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(jwtSettings.getTokenHeaderParam(), "Bearer "+token);
		
		ResponseEntity<String> entity = template.exchange("/actuator/auditevents", HttpMethod.GET, new HttpEntity<>(headers), String.class);
		
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		System.out.println(entity.getBody());
	}
	
	@Test
	public void testSnoop() throws Exception {

		String token = jwtTokenService.generateMSToken();
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(jwtSettings.getTokenHeaderParam(), "Bearer "+token);
		
		ResponseEntity<String> entity = template.exchange("/api/snoop", HttpMethod.GET, new HttpEntity<>(headers), String.class);
		
		
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody()).isNotEmpty();
		
		System.out.println(entity.getBody());
	}

	
	@Test
	public void testSnoopUnauthorized() throws Exception {

		HttpHeaders headers = new HttpHeaders();
		headers.add(jwtSettings.getTokenHeaderParam(), "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqY2UtZGVtby1zZWN1cmUtbXZuMSIsInNjb3BlcyI6WyJST0xFX1RSVVNURURfQ0xJRU5UIiwiUk9MRV9BQ1RVQVRPUiJdLCJpc3MiOiJqY2UtZGVtby1zZWN1cmUtbXZuMSIsImlhdCI6MTQ4ODE1NTQ3NSwiZXhwIjoxNDg4MTU2Mzc1fQ.rkxyjijooZ0PG7s2yZLy8jhWe1KoEbRTpA-VyUMVFw8");
		
		ResponseEntity<String> entity = template.exchange("/api/snoop", HttpMethod.GET, new HttpEntity<>(headers), String.class);
		
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		//assertThat(entity.getBody()).isNotEmpty();
		
		System.out.println(entity.getBody());
	}

}
