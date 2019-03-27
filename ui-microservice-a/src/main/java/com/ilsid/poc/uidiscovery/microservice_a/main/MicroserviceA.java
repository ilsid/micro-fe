package com.ilsid.poc.uidiscovery.microservice_a.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 
 * @author illia.sydorovych
 *
 */
@SpringBootApplication
public class MicroserviceA extends SpringBootServletInitializer {

	public static void main(String[] args) {
		new MicroserviceA().configure(new SpringApplicationBuilder(MicroserviceA.class)).run(args);
	}

}
