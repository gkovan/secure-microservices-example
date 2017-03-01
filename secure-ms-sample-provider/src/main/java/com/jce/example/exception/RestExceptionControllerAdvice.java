package com.jce.example.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionControllerAdvice {

	@ExceptionHandler(Throwable.class)
    public ResponseEntity<?> handleThrowable(Throwable ex) {
				
		ErrorMessage errorMessage = new ErrorMessage();	
		setHttpStatus(ex, errorMessage);
		errorMessage.setCode(500);
		errorMessage.setMessage(ex.getMessage());
		StringWriter errorStackTrace = new StringWriter();
		ex.printStackTrace(new PrintWriter(errorStackTrace));
		errorMessage.setDeveloperMessage(errorStackTrace.toString());
		
		return ResponseEntity.status(errorMessage.getStatus()).contentType(org.springframework.http.MediaType.APPLICATION_JSON).body(errorMessage);
		
//		return Response.status(errorMessage.getStatus())
//				.entity(errorMessage)
//				.type(MediaType.APPLICATION_JSON)
//				.build();
    }
	
	private void setHttpStatus(Throwable ex, ErrorMessage errorMessage) {
		//if(ex instanceof WebApplicationException ) {
		//	errorMessage.setStatus(((WebApplicationException)ex).getResponse().getStatus());
		//} else {
			errorMessage.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value()); //defaults to internal server error 500
		//}
	}	
	
}
