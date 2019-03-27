package com.ilsid.poc.uidiscovery.websocket;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.ilsid.poc.uidiscovery.common.JsonUtil;
import com.ilsid.poc.uidiscovery.dao.EventDAO;
import com.ilsid.poc.uidiscovery.dao.UIComponentDAO;
import com.ilsid.poc.uidiscovery.model.Event;
import com.ilsid.poc.uidiscovery.model.PublishedEvent;
import com.ilsid.poc.uidiscovery.model.SubscribedEvent;
import com.ilsid.poc.uidiscovery.model.UIComponent;

/**
 * 
 * @author illia.sydorovych
 *
 */
public class RegistryWebsocketHandler extends TextWebSocketHandler {

	private static final String EMPTY_STR = "";

	private static final String BLANK_REGEXP = "\\s+";

	private static final String OPERATION_FIELD_TPLT = "\"operation\":\"%s\"";

	private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

	@Autowired
	private UIComponentDAO componentDao;

	@Autowired
	private EventDAO eventDao;

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
	}

	public void broadcast(String message) throws IOException {
		for (WebSocketSession session : sessions) {
			session.sendMessage(new TextMessage(message));
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String messageBody = message.getPayload();

		if (isOperation(messageBody, Operations.GET_ALL_COMPONENTS)) {
			sendAll(Operations.GET_ALL_COMPONENTS, session);

		} else if (isOperation(messageBody, Operations.STORE_SUBSCRIBED_EVENT)) {
			
			EventRequest request = JsonUtil.toObject(messageBody, EventRequest.class);
			final String eventType = request.getEventType();
			UIComponent comp = componentDao.findByComponentId(request.getComponentId());
			storeEvent(eventType, comp, comp.getSubscribedEvents(), new SubscribedEvent(eventType), session);
			
		} else if (isOperation(messageBody, Operations.STORE_PUBLISHED_EVENT)) {
			
			EventRequest request = JsonUtil.toObject(messageBody, EventRequest.class);
			final String eventType = request.getEventType();
			UIComponent comp = componentDao.findByComponentId(request.getComponentId());
			storeEvent(eventType, comp, comp.getPublishedEvents(), new PublishedEvent(eventType), session);
			
		}
	}

	private <T extends Event> void storeEvent(String eventType, UIComponent comp, List<T> events, T event,
			WebSocketSession session) throws IOException {

		boolean eventAlreadyStored = events.stream().anyMatch(e -> eventType.equals(e.getTypeName()));

		if (!eventAlreadyStored) {
			// FIXME: make it transactional (via EntityManager.getTransaction() or via declarative tx)
			eventDao.save(event);
			events.add(event);
			componentDao.save(comp);

			sendAll(Operations.REFRESH_ALL_COMPONENTS, session);
		}
	}

	private boolean isOperation(String message, String operation) {
		return message != null
				&& message.replaceAll(BLANK_REGEXP, EMPTY_STR).contains(String.format(OPERATION_FIELD_TPLT, operation));
	}

	private void sendAll(String operation, WebSocketSession session) throws IOException {
		Iterable<UIComponent> components = componentDao.findAll();
		final WebsocketResponse response = new WebsocketResponse(operation, components);
		String responseRaw = JsonUtil.toJsonString(response);
		session.sendMessage(new TextMessage(responseRaw));
	}

}
