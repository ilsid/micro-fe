package com.ilsid.poc.uidiscovery.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ilsid.poc.uidiscovery.model.UIComponent;

@Repository
public interface UIComponentDAO extends CrudRepository<UIComponent, Long> {
	
	@Transactional
	@Modifying
	@Query("DELETE FROM UIComponent c WHERE c.componentId=:componentId")
	void deleteByComponentId(@Param("componentId") String componentId);
	
	@Query("FROM UIComponent c WHERE c.componentId=:componentId")
	UIComponent findByComponentId(@Param("componentId") String componentId);
	
}
