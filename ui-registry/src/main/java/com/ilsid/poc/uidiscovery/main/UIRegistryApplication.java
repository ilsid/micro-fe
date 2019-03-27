package com.ilsid.poc.uidiscovery.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.core.JmsTemplate;

import com.ilsid.poc.uidiscovery.messaging.ActiveMQConfig;
import com.ilsid.poc.uidiscovery.service.JerseyConfig;
import com.ilsid.poc.uidiscovery.websocket.WebsocketConfig;

/**
 * 
 * @author illia.sydorovych
 *
 */
@SpringBootApplication
@Import({ WebsocketConfig.class, JerseyConfig.class, ActiveMQConfig.class })
@EnableJpaRepositories("com.ilsid.poc.uidiscovery.dao")
@EntityScan("com.ilsid.poc.uidiscovery.model")
@ComponentScan({"com.ilsid.poc.uidiscovery.messaging"})
public class UIRegistryApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = new UIRegistryApplication().configure(new SpringApplicationBuilder(UIRegistryApplication.class)).run(args);
		
		JmsTemplate jmsTemplate = (JmsTemplate) context.getBean("jmsTopicTemplate");
		jmsTemplate.convertAndSend("registryTopic", "Test topic message");
	}

}
