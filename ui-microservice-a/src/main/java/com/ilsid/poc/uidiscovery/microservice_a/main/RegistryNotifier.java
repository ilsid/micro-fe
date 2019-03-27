package com.ilsid.poc.uidiscovery.microservice_a.main;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ilsid.poc.uidiscovery.microservice_a.model.UIComponent;

/**
 * 
 * @author illia.sydorovych
 *
 */
@Component
public class RegistryNotifier {

	@Autowired
	@Qualifier("uiRegistryRestTemplate")
	private RestTemplate restTemplate;

	@Value("${uiregistry.endpoint}")
	private String registryEndpoint;

	@Value("${component.url}")
	private String url;
	
	@Value("${component.id}")
	private String componentId;
	

	@PostConstruct
	public void registerItself() {
		UIComponent message = new UIComponent();
		message.setComponentId(componentId);
		message.setUri(url);
		restTemplate.exchange(registryEndpoint, HttpMethod.POST, createEntity(message), Void.class);
	}

	@PreDestroy
	public void removeItself() {
		UIComponent message = new UIComponent();
		message.setComponentId(componentId);
		message.setUri(url);
		restTemplate.exchange(registryEndpoint, HttpMethod.DELETE, createEntity(message), Void.class);
	}

	private <T> HttpEntity<T> createEntity(T obj) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<T> entity = new HttpEntity<>(obj, headers);

		return entity;
	}

}
