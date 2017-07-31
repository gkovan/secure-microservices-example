# secure_offline_microservices

Note: This example is based on the github project: https://github.com/jedward19/bff-secure_offline_microservices

This example consists of two microservices:
 - (microservice 1) secure-ms-sample-consumer.
 - (microservice 2) secure-ms-sample-provider.

The consumer calls the provider using a REST call.
The invokation is secured using JWT tokens.
JWT stands for JSON Web Tokens. See http://jwt.io for more info.
The JWT java library used in this example is jjwt (https://github.com/jwtk/jjwt).

In this example, when the consumer invokes the provider using a REST call, the consumer 
generates a JWT token and signs it using a secret shared key.  The JWT token gets put onto the
the HTTP header and is transmitted along with the rest of the HTTP request.

When the provider service receives the request, the provider first checks to ensure that the
JWT is valid by verifying the signature using the secret shared key.  If the provider can verify the signature
successfully then it means that it trusts the requester and resumes processing the request.

Building and running the sample on Bluemix:
-------------------------------------------
Step 0: Configure the example
   - Configure the name and host of the provider microservice.
      - Open file \secure-ms-sample-provider\manifest.yml and update the name and host properties to your unique values.

   - Configure the name and host of the consumer microservice.
      - Open file \secure-ms-sample-consumer\manifest.yml and update the name and host properties to your unique values.

   - Configure the consumer application.yml file (\secure-ms-sample-consumer\src\main\resources\application.yml)
      - This file is used to configure the consumer microservice.
      - Update the properties:
             
             - microservice.endpoint.ms1  
             - microservice.endpoint.ms2 
             
         with the value of the provider host you configured in the provider's manifest.yml file.
      
      - This property is used by the consumer to create the url to call the provider.
      - Note: this property appears twice on lines 15,16 and also on 57,58.  Make sure to update all occurances.

Step 1: Build and deploy the consumer microservice:
   - From the to the 'bff-secure_offline_microservices-master\secure-ms-sample-consumer' directory
     run the gradle build command:
       > gradlew build
   - Gradle generates a war file in folder  ...\secure-ms-sample-consumer\build\libs folder.
   - Push the consumer microservice war file to Bluemix:
       > cf push  
       Note: The cf push assumes you are logged into your Bluemix account.
             The details of how the app is deployed is in the manifest.yml file.

Step 2: Build and deploy the provider microservice:
   - From the to the 'bff-secure_offline_microservices-master\secure-ms-sample-provider' directory
     run the gradle build command:
       > gradlew build
   - Gradle generates a war file in folder  ...\secure-ms-sample-provider\build\libs folder.
   - Push the provider microservice war file to Bluemix:
       > cf push  
       Note: The cf push assumes you are logged into your Bluemix account.
             The details of how the app is deployed is in the manifest.yml file.


Implementation Details of the Consumer Microservice:
----------------------------------------------------
The Rest controller is implemented by com.ibm.microservice.sample.rs.IndexRestController class.
The method public:  public String index() has the @RequestMapping("/")
  - This method returns an html document with 3 url references:
     url 1: Test Token - generates a JWT token and displays it in the browser.
     url 2: Snoop Token - calls the provider, passing a secure signed JWT token
     url 3: Exception - ???

When url 2 (Snoop Token request) is invoked, the following processing occurs:
   - IndexRestController.java -> method snoopClient() gets invoked.
     - This method does the following things:
         - calls the JWTTokenService -> generateMSToken() method
            - this method uses the jjwt library to create and sign the JWT token.
         - adds the JWT token to the HTTP header
         - uses the Spring Boot RestTemplate client to invoke snoop REST call passing the HTTP request 
           which includes the JWT token in the header.

Implementation Details of the Provider Microservice:
----------------------------------------------------
The Rest controller for the provider is com.ibm.microservice.sample.rs.SnoopResource.java.
Before the Rest Controller executes, JWT filter processing is executed to validate the JWT token.

The following is a description of how the JWT filter processing works which leverages the Spring Security framework.


The WebSecurityConfig class extends BaseWebSecurityConfig class which extends the Spring Security WebSecurityConfigureAdapter class.
The WebSecurityConfig class configure method, sets a filter for the JWT authentication processing filter.
This filter executes before the actual REST request is invoked and is responsible for getting the JWT token
from the header and verifying that the token is valid.
When a REST request is invoked, the following processing occurs:
  - JWTAuthenticationProcessingFilter.java -> method attemptAuthentication(...)
    - JWTAuthenticationProvider.java -> method authentication(...)
      - JWTTokenService.java -> method parseClaims(...)
        - This is the actual method that verifies the JWT token.
        - This method uses the jjwt library.
