package com.ibm.microservice.sample;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication(
		scanBasePackages = { "com.ibm.microservice.sample","com.ibm.microservice.security"}
)
public class ServletInitializer extends SpringBootServletInitializer {


}
