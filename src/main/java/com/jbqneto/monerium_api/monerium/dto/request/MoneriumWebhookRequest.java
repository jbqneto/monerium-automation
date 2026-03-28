package com.jbqneto.monerium_api.monerium.dto.request;

public record MoneriumWebhookRequest(
    String eventType,
    String eventId,
    String orderId,
    String accountId,
    String payload
) {}
