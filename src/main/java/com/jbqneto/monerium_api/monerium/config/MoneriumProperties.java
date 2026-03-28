package com.jbqneto.monerium_api.monerium.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.monerium")
public record MoneriumProperties(
    String environment,
    String apiUrl,
    String webUrl,
    String authorizationClientId,
    String clientCredentialsClientId,
    String clientSecret,
    String webhookSecret,
    String redirectUri,
    String defaultChain,
    String defaultWalletAddress,
    String apiVersionAcceptHeader,
    Integer connectTimeoutSeconds,
    Integer readTimeoutSeconds
) {}
