package com.ilsid.poc.uidiscovery.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SUBSRIBED_EVENT")
public class SubscribedEvent extends Event {

	public SubscribedEvent() {
		super();
	}

	public SubscribedEvent(String typeName) {
		super(typeName);
	}

}
