package com.jce.example.exception;

import java.io.Serializable;
import java.util.Date;

public class ErrorMessage implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date timestamp;
	private int status;
	private int code;
	private String message;
	private String developerMessage;
	
	public ErrorMessage() {
		this(new Date(), 0,0, null, null);
	}

	public ErrorMessage(int status, int code, String message, String developerMessage) {
		this(new Date(), status,code, message, developerMessage);
	}

	public ErrorMessage(Date timestamp, int status, int code, String message, String developerMessage) {
		this.timestamp = timestamp;
		this.status = status;
		this.code = code;
		this.message = message;
		this.developerMessage = developerMessage;
	}

	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDeveloperMessage() {
		return developerMessage;
	}
	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
}
