package com.jbqneto.monerium_api.monerium.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jbqneto.monerium_api.monerium.dto.MoneriumWebhookEventType;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MoneriumSubscriptionCreatedWebhookRequest.class, name = "subscription.created"),
    @JsonSubTypes.Type(value = MoneriumOrderCreatedWebhookRequest.class, name = "order.created"),
    @JsonSubTypes.Type(value = MoneriumOrderUpdatedWebhookRequest.class, name = "order.updated"),
    @JsonSubTypes.Type(value = MoneriumProfileUpdatedWebhookRequest.class, name = "profile.updated"),
    @JsonSubTypes.Type(value = MoneriumIbanUpdatedWebhookRequest.class, name = "iban.updated")
})
public sealed interface MoneriumWebhookRequest permits
    MoneriumSubscriptionCreatedWebhookRequest,
    MoneriumOrderCreatedWebhookRequest,
    MoneriumOrderUpdatedWebhookRequest,
    MoneriumProfileUpdatedWebhookRequest,
    MoneriumIbanUpdatedWebhookRequest {

    MoneriumWebhookEventType type();

    Instant timestamp();
}
