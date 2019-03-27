package com.ilsid.poc.uidiscovery.microservice_b.main;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author illia.sydorovych
 *
 */
@Configuration
public class ApplicationConfig {

	private static final int REST_CLIENT_TIMEOUT = 60000;

	@Value("${uiregistry.host}")
	private String registryHost;

	@Bean(name = "uiRegistryRestTemplateBuilder")
	public RestTemplateBuilder uiRegistryRestTemplateBuilder() {
		return new RestTemplateBuilder().rootUri(registryHost).setReadTimeout(REST_CLIENT_TIMEOUT);

	}

	@Bean(name = "uiRegistryRestTemplate")
	public RestTemplate uiRegistryRestTemplate() {
		RestTemplate template = uiRegistryRestTemplateBuilder().build();
		ClientHttpRequestFactory factory = new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory());
		template.setRequestFactory(factory);
		List<ClientHttpRequestInterceptor> interceptors = new LinkedList<>();
        interceptors.add(new LoggingRequestInterceptor());
        template.setInterceptors(interceptors);

		return template;
	}

}
