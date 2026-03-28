package com.jbqneto.monerium_api.monerium.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jbqneto.monerium_api.monerium.dto.MoneriumWebhookEventType;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MoneriumOrderUpdatedWebhookRequest(
    MoneriumWebhookEventType type,
    Instant timestamp,
    MoneriumOrderWebhookData data
) implements MoneriumWebhookRequest {}
