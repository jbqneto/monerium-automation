package com.jbqneto.monerium_api.monerium.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MoneriumWebhookSubscriptionState {
    @JsonProperty("active")
    ACTIVE,

    @JsonProperty("inactive")
    INACTIVE
}
