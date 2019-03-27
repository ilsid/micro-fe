package com.ilsid.poc.uidiscovery.websocket;

public class EventRequest extends BasicRequest {
	
	private String componentId;
	
	private String eventType;

	
	public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	
}
