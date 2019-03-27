package com.ilsid.poc.uidiscovery.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 
 * @author illia.sydorovych
 *
 */
@Entity
@Table(name = "UI_COMPONENT")
public class UIComponent {

	@Id
	@GeneratedValue
	@Column
	private Long id;

	@Column
	private String componentId;

	@Column
	private String uri;

	@Column
	private String operation;

	@Column
	private String layout;

	@OneToMany(fetch = FetchType.EAGER, targetEntity = Event.class)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<SubscribedEvent> subscribedEvents = new LinkedList<>();
	
	@OneToMany(fetch = FetchType.EAGER, targetEntity = Event.class)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<PublishedEvent> publishedEvents = new LinkedList<>();
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
	
	public List<SubscribedEvent> getSubscribedEvents() {
		return subscribedEvents;
	}

	public void setSubscribedEvents(List<SubscribedEvent> subscriptionEvents) {
		this.subscribedEvents = subscriptionEvents;
	}
	
	public List<PublishedEvent> getPublishedEvents() {
		return publishedEvents;
	}

	public void setPublishedEvents(List<PublishedEvent> publishedEvents) {
		this.publishedEvents = publishedEvents;
	}

}