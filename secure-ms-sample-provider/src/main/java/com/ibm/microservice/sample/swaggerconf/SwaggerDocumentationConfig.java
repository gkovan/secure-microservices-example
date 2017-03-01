package com.ibm.microservice.sample.swaggerconf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerDocumentationConfig {
    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("JCE Demo Secure MVN1 API")
            .description("A sample API that demonstrate Microservice JWT security and swagger-2.0 specification")
            //.license("Creative Commons 4.0 International")
            //.licenseUrl("http://creativecommons.org/licenses/by/4.0/")
            //.termsOfServiceUrl("http://swagger.io")
            .version("0")
            .contact(new Contact("","", ""))
            .build();
    }

    @Bean
    public Docket customImplementation(){
        return new Docket(DocumentationType.SWAGGER_2)
        		.select()                                  
                .apis(RequestHandlerSelectors.any())              
                .paths(PathSelectors.any())                          
                .build(); 	
//                .select()
//                    .apis(RequestHandlerSelectors.basePackage("com.ibm.microservice.sample.rs"))
//                    .build()
//                .directModelSubstitute(org.joda.time.LocalDate.class, java.sql.Date.class)
//                .directModelSubstitute(org.joda.time.DateTime.class, java.util.Date.class)
//                .apiInfo(apiInfo());
    }
}
