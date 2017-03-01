package com.jce.example.rs;

import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

//@XmlRootElement
public class SnoopResponse implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@XmlElement
	private Map<String,Object> map;
	
	public SnoopResponse(Map<String, Object> map) {
		this.map = map;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

}
