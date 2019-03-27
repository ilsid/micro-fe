package com.ilsid.poc.uidiscovery.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PUBLISHED_EVENT")
public class PublishedEvent extends Event {
	
	public PublishedEvent() {
		super();
	}

	public PublishedEvent(String typeName) {
		super(typeName);
	}
	
}
