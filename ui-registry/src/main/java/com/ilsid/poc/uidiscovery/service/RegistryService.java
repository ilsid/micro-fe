package com.ilsid.poc.uidiscovery.service;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.ilsid.poc.uidiscovery.common.JsonUtil;
import com.ilsid.poc.uidiscovery.dao.UIComponentDAO;
import com.ilsid.poc.uidiscovery.model.UIComponent;
import com.ilsid.poc.uidiscovery.websocket.RegistryWebsocketHandler;

/**
 * 
 * @author illia.sydorovych
 *
 */
@Path("/")
public class RegistryService {

	private static final String REGISTER_PATH = "register";
	
	private static final String OPERATION_REGISTER = "register";
	
	private static final String OPERATION_REMOVE = "remove";
	
	@Autowired
	private UIComponentDAO dao;
	
	@Autowired
	private RegistryWebsocketHandler websocket;
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(REGISTER_PATH)
	public void create(UIComponent component) {
		try {
			boolean registered = dao.findByComponentId(component.getComponentId()) != null;
			if (registered) {
				return;
			}
			
			dao.save(component);
			component.setOperation(OPERATION_REGISTER);
			websocket.broadcast(JsonUtil.toJsonString(component));
		} catch (IOException e) {
			new AppException(REGISTER_PATH, e, "Broadcast failure");
		}
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Path(REGISTER_PATH)
	public void delete(UIComponent component) {
		try {
			dao.deleteByComponentId(component.getComponentId());
			component.setOperation(OPERATION_REMOVE);
			websocket.broadcast(JsonUtil.toJsonString(component));
		} catch (IOException e) {
			new AppException(REGISTER_PATH, e, "Broadcast failure");
		}
	}
	
}
