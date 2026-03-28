package com.jbqneto.monerium_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.telegram")
public record TelegramProperties(
    boolean enabled,
    String botToken,
    String chatId,
    Integer connectTimeoutSeconds,
    Integer readTimeoutSeconds
) {}
