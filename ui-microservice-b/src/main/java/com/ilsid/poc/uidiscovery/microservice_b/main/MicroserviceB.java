package com.ilsid.poc.uidiscovery.microservice_b.main;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 
 * @author illia.sydorovych
 *
 */
@SpringBootApplication
public class MicroserviceB extends SpringBootServletInitializer {

	public static void main(String[] args) {
		new MicroserviceB().configure(new SpringApplicationBuilder(MicroserviceB.class)).run(args);
	}

}
