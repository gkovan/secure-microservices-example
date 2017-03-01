package com.ibm.microservice.sample.rs.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


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
public class TokenTestGeneratorTest {

	@Autowired
	private TestRestTemplate template;
	
	
	@Test
	public void testToken() throws Exception {

		ResponseEntity<String> entity = template.getForEntity("/api/token", String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody()).isNotEmpty();
		
		System.out.println(entity.getBody());
	}
	
	@Test
	public void testActuatorToken() throws Exception {

		ResponseEntity<String> entity = template.getForEntity("/api/actuatortoken", String.class);
		assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(entity.getBody()).isNotEmpty();
		
		System.out.println(entity.getBody());
	}

}
