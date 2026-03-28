package com.jbqneto.monerium_api.monerium.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class MoneriumClientConfiguration {

    /**
     * Centralizes Monerium HTTP client configuration so all Monerium clients
     * share the same base URL, timeouts, and default headers.
     */
    @Bean
    @Primary
    @Qualifier("moneriumRestClient")
    RestClient moneriumRestClient(RestClient.Builder builder, MoneriumProperties properties) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.connectTimeoutSeconds() * 1000);
        requestFactory.setReadTimeout(properties.readTimeoutSeconds() * 1000);

        return builder
            .baseUrl(properties.apiUrl())
            .requestFactory(requestFactory)
            .defaultHeader(HttpHeaders.ACCEPT, properties.apiVersionAcceptHeader())
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }

    @Bean
    @Qualifier("moneriumClientCredentials")
    RestClient moneriumClientAuthRestClient(RestClient.Builder builder, MoneriumProperties properties) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.connectTimeoutSeconds() * 1000);
        requestFactory.setReadTimeout(properties.readTimeoutSeconds() * 1000);

        return builder
                .baseUrl("https://api.monerium.dev")
                .requestFactory(requestFactory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .build();
    }
}
