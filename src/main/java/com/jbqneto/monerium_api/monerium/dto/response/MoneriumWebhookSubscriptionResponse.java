package com.jbqneto.monerium_api.monerium.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jbqneto.monerium_api.monerium.dto.MoneriumWebhookEventType;
import com.jbqneto.monerium_api.monerium.dto.MoneriumWebhookSubscriptionState;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MoneriumWebhookSubscriptionResponse(
    UUID id,
    URI url,
    List<MoneriumWebhookEventType> types,
    MoneriumWebhookSubscriptionState state
) {}
