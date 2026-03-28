package com.jbqneto.monerium_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.executor")
public record ExecutorProperties(
    String baseUrl,
    String apiKey,
    Integer connectTimeoutSeconds,
    Integer readTimeoutSeconds
) {}
