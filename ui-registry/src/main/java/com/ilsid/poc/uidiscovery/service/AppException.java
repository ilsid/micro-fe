package com.ilsid.poc.uidiscovery.service;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import com.ilsid.poc.uidiscovery.common.ExceptionUtil;


/**
 *  
 * @author illia.sydorovych
 *
 */
public class AppException extends WebApplicationException {

	private static final long serialVersionUID = -3798165233403070956L;

	private String message;

	private Status status = Status.BAD_REQUEST;

	private boolean hasCause;

	private String path;

	private Object entity;

	public AppException(String path, Throwable e) {
		super(e);
		this.path = path;
		hasCause = true;
	}

	public AppException(String path, Throwable e, Status status, Object entity) {
		super(e);
		this.path = path;
		hasCause = true;
		this.status = status;
		this.entity = entity;
	}
	
	public AppException(String path, Throwable e, Object entity) {
		super(e);
		this.path = path;
		hasCause = true;
		this.entity = entity;
	}

	public AppException(String path, String message, Status status) {
		this.path = path;
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}

	public Status getStatus() {
		return status;
	}

	public boolean hasCause() {
		return hasCause;
	}

	public Throwable getActualCause() {
		return hasCause ? this.getCause() : this;
	}

	public Object getEntity() {
		if (entity == null) {
			return ExceptionUtil.getExceptionMessageChain(getActualCause());
		} else {
			return entity;
		}
	}

}
