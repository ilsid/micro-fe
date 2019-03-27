package com.ilsid.poc.uidiscovery.messaging;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class RegistryTopicListener {
	
	@JmsListener(destination = "registryTopic", containerFactory = "topicListenerFactory")
	public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");
    }
	
}
