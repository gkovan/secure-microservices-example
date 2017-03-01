package com.ibm.microservice.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication
@SpringBootApplication(
		scanBasePackages = { "com.ibm.microservice.sample","com.ibm.microservice.security"}
)
public class DemoSecureApplication {

//	@Bean
//	 public RestTemplateBuilder restTemplateBuilder() {
//	   return new RestTemplateBuilder()
//	        .rootUri("/");
//	  }
	
	public static void main(String[] args) {
		SpringApplication.run(DemoSecureApplication.class, args);
	}
}
