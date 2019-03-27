package com.ilsid.poc.uidiscovery.microservice_a.model;

/**
 * 
 * @author illia.sydorovych
 *
 */
public class UIComponent {

	private String componentId;

	private String uri;

	private String operation;
	
    private String layout;


    public String getComponentId() {
		return componentId;
	}

	public void setComponentId(String componentId) {
		this.componentId = componentId;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}



}
