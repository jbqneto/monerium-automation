package com.jbqneto.monerium_api.monerium.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MoneriumWebhookSubscriptionsResponse(
    List<MoneriumWebhookSubscriptionResponse> subscriptions
) {}
