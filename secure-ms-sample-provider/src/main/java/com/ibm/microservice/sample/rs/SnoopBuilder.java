package com.ibm.microservice.sample.rs;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SnoopBuilder {

	private static Map<String, Object> getEnvironment(){
		Map<String, Object> map = new LinkedHashMap<String,Object>();
		
		Map<String, String> envMap = System.getenv();
		for (String key : envMap.keySet()) {
			
//			if("VCAP_SERVICES".equals(key)){
//				try {
//					
//					String vcapServices = envMap.get(key);
//					System.out.println("["+vcapServices+"]");
//					JSONObject obj = new JSONObject(vcapServices);
//					map.put(key, obj.toString(1));					
//				} catch (Exception e) {
//					e.printStackTrace();
//					map.put(key, "Problem parsing VCAP_SERVICES");
//				}
//			}else{
				map.put(key, envMap.get(key));	
//			}
		}
		
		return map;
	}
	
	private static Map<String, Object> getRequestInformation(HttpServletRequest req){
		Map<String, Object> map = new LinkedHashMap<String,Object>();
		
		map.put( "Request method", req.getMethod());
		map.put( "Request URI", req.getRequestURI());
		map.put( "Request protocol", req.getProtocol());
		map.put( "Servlet path", req.getServletPath());
		map.put( "Path info", req.getPathInfo());
		map.put( "Path translated", req.getPathTranslated());
		map.put( "Character encoding", req.getCharacterEncoding());
		map.put( "Query string", req.getQueryString());
		map.put( "Content length", ""+req.getContentLength());
		map.put( "Content type", req.getContentType());
		
		InetAddress address = null;
		
		try {address = InetAddress.getLocalHost();} catch (UnknownHostException e1) {}
		
		if(address!=null){
			map.put( "InetAddress.getLocalHost", address.getHostAddress());
		}else{
			map.put( "Local Server name", req.getLocalAddr());
		}
		
		map.put( "Server name", req.getServerName());
		
		map.put( "Server port", ""+req.getServerPort());
		map.put( "Remote user", req.getRemoteUser());
		map.put( "Remote address", req.getRemoteAddr());
		map.put( "Remote host", req.getRemoteHost());
		map.put( "Authorization scheme", req.getAuthType());
		if (req.getLocale() != null)
		{
			map.put( "Preferred Client Locale", req.getLocale().toString());
		}
		else
		{
			map.put( "Preferred Client Locale", "none");
		}
		Enumeration<Locale> ee = req.getLocales();
		ArrayList<String> locales = new ArrayList<String>();
		while ((ee!=null)&&(ee.hasMoreElements()))
		{
			Locale cLocale = (Locale)ee.nextElement();
			if (cLocale != null)
			{
				locales.add(cLocale.toString());
			}
			else
			{
				locales.add("none");
			}
		}
		map.put( "All Client Locales", locales.toString());
		map.put( "Context Path", req.getContextPath());
		if (req.getUserPrincipal() != null)
		{
			map.put( "User Principal", req.getUserPrincipal().getName());
		}
		else
		{
			map.put( "User Principal", "none");
		}
	
		Enumeration<String> e = req.getHeaderNames();
		HashMap<String,String> headerMap = new HashMap<String,String>();
		
		if ( (e!=null)&&(e.hasMoreElements()) )
		{
			while ( e.hasMoreElements() )
			{
				String name = e.nextElement();
				headerMap.put(name, req.getHeader(name));
			}
		}
		map.put("Request Headers", headerMap);
	
		e = req.getParameterNames();
		HashMap<String,String> parameterMap = new HashMap<String,String>();
		if ( (e!=null) &&(e.hasMoreElements()) )
		{
			while ( e.hasMoreElements() )
			{
				String name = e.nextElement();
				parameterMap.put(name, req.getParameter(name));
			}
		}
				
		e = req.getParameterNames();
		if ( (e!=null)&&(e.hasMoreElements()) )
		{
			while ( e.hasMoreElements() )
			{
				String name = e.nextElement();
				String vals[] = (String []) req.getParameterValues(name);
				
				ArrayList<String> params = new ArrayList<String>();
				
				if ( vals != null )
				{
					for ( int i = 0; i<vals.length; i++ ){
						params.add(vals[i]);
					}
				}
				parameterMap.put(name, params.toString());
			}
		}
		map.put("Request Parameters", parameterMap);
		
		return map;
	}

	private static Map<String, String> getSSLCipherSuite(HttpServletRequest req){
		Map<String, String> map = new LinkedHashMap<String,String>();
		
		String  cipherSuite = (String)req.getAttribute ("javax.net.ssl.cipher_suite");
		if ( cipherSuite != null )
		{
			X509Certificate certChain [] = (X509Certificate [])req.getAttribute ("javax.net.ssl.peer_certificates");
	
			
			map.put("Cipher Suite", cipherSuite);
	
			if ( certChain != null )
			{
				for ( int i = 0; i < certChain.length; i++ )
				{
					map.put ("client cert chain [" + i + "] = " , certChain [i].toString ());
				}
			}
		}
		
		return map;
	}

	private static Map<String, String> getCookie(HttpServletRequest req){
		Map<String, String> map = new LinkedHashMap<String,String>();
		
		Cookie[] cookies = req.getCookies();
		if ( cookies != null && cookies.length > 0 )
		{
			for ( int i=0; i<cookies.length; i++ )
			{
				map.put(cookies[i].getName(), cookies[i].getValue() );
			}
		}
		return map;
	}

	private static Map<String, Object> getRequestattributes(HttpServletRequest req){
		Map<String, Object> map = new LinkedHashMap<String,Object>();
		
		Enumeration<String> e = req.getAttributeNames();
		if ( e.hasMoreElements() )
		{
			while ( e.hasMoreElements() )
			{
				String name = e.nextElement();
				map.put( name , req.getAttribute(name).toString());
			}
		}
		
		return map;
	}

	private static Map<String, Object> getServletContextAttributes(ServletContext servCtxt,HttpServletRequest req){
		Map<String, Object> map = new LinkedHashMap<String,Object>();
		
		Enumeration<String> e = servCtxt.getAttributeNames();
		if ( (e!=null) && (e.hasMoreElements()) )
		{
			while ( e.hasMoreElements() )
			{
				String name = e.nextElement();
				String value = ""+servCtxt.getAttribute(name);
				
				map.put(name, value);
			}
		}
		return map;
	}

	private static Map<String, Object> getSessioninformation(HttpServletRequest req){
		Map<String, Object> map = new LinkedHashMap<String,Object>();
		
		HttpSession session = req.getSession(false);
		if ( session != null )
		{
			map.put( "Session ID", session.getId());
			map.put( "Last accessed time", new Date(session.getLastAccessedTime()).toString());
			map.put( "Creation time", new Date(session.getCreationTime()).toString());
			String mechanism = "unknown";
			if ( req.isRequestedSessionIdFromCookie() )
			{
				mechanism = "cookie";
			}
			else if ( req.isRequestedSessionIdFromURL() )
			{
				mechanism = "url-encoding";
			}
			map.put( "Session-tracking mechanism", mechanism);
	
			Enumeration<String> vals = session.getAttributeNames();
			Map<String, Object> sessionValues = new LinkedHashMap<String,Object>();
			
			if ((vals!=null) && (vals.hasMoreElements()))
			{
				while (vals.hasMoreElements())
				{
					String name = vals.nextElement();
					sessionValues.put( name,  session.getAttribute(name));
				}
			}
			map.put("Session Values", sessionValues);
		}
		return map;
	}

	public static Map<String, Object> getSnoop (ServletContext servletContext,HttpServletRequest req)
	{
		Map<String, Object> map = new LinkedHashMap<String,Object>();
		map.put("Requested URL", req.getRequestURL().toString());
		map.put("Request Information", getRequestInformation(req));
		
		map.put("Request SSL Cipher Suite", getSSLCipherSuite(req));
		map.put("Request Cookie(s)", getCookie(req));
		map.put("Request Attributes", getRequestattributes(req));
		map.put("Request Servlet Context", getServletContextAttributes(servletContext, req));
		map.put("Request Session Information", getSessioninformation(req));
		map.put("Get Environment Information", getEnvironment());
		
		return map;
	
	}

}
