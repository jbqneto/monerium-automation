package com.jbqneto.monerium_api.monerium.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MoneriumWebhookEventType {
    @JsonProperty("subscription.created")
    SUBSCRIPTION_CREATED,

    @JsonProperty("order.created")
    ORDER_CREATED,

    @JsonProperty("order.updated")
    ORDER_UPDATED,

    @JsonProperty("profile.updated")
    PROFILE_UPDATED,

    @JsonProperty("iban.updated")
    IBAN_UPDATED
}
