package com.ilsid.poc.uidiscovery.websocket;

public class WebsocketResponse {
	
	private String operation;
	
	private Object payload;
	

	public WebsocketResponse(String operation, Object payload) {
		this.operation = operation;
		this.payload = payload;
	}
	
	public String getOperation() {
		return operation;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public Object getPayload() {
		return payload;
	}
	
	public void setPayload(Object payload) {
		this.payload = payload;
	}
	
}
