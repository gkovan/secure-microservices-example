package com.ibm.microservice.sample.rs;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/snoop")
public class SnoopResource {
	private static final Logger logger = LoggerFactory.getLogger(SnoopResource.class);
	
	@Autowired private ServletContext servletContext;
	
	public SnoopResource() {
	}
	
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<SnoopResponse> getSnoopInfo(HttpServletRequest httpReq) {
		
		logger.info("getSnoopInfo");
						
		String msg = getClientSourceAddress(httpReq);
		logger.info("What a beatiful day. ["+msg+"]");
		
		
		Map<String,Object> map = SnoopBuilder.getSnoop(servletContext, httpReq);
		logger.info("Snoop output. ["+map+"]");
		
//		boolean debug = true;
//		if(debug){
//			throw new RuntimeException ("This is a test");	
//		}
		
		
		//Response response = Response.status(Response.Status.OK).entity("Template Base URL called ").build();
		//Response response = Response.ok(map).build();	
		return ResponseEntity.ok(new SnoopResponse(map));
		
		//return new SnoopResponse(map);
	}
	
	protected String getClientSourceAddress(HttpServletRequest httpReq){
		String soureInfo = null;
		try {
			soureInfo = httpReq.getHeader("X-Forwarded-For").split(",")[0];
		} catch (Exception ignored){}
		
		if((soureInfo==null) || (soureInfo.trim().isEmpty())){
			soureInfo = httpReq.getRemoteAddr();
		}

		return soureInfo;
	}	
}
