package com.ilsid.poc.uidiscovery.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ilsid.poc.uidiscovery.model.Event;

@Repository
public interface EventDAO extends CrudRepository<Event, Long> {
	
}
