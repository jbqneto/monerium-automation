package com.jbqneto.monerium_api.monerium.client;

import com.jbqneto.monerium_api.monerium.dto.request.MoneriumWebhookSubscriptionRequest;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumWebhookSubscriptionResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumWebhookSubscriptionsResponse;

public interface MoneriumWebhookClient {

    MoneriumWebhookSubscriptionsResponse listSubscriptions(String accessToken);

    MoneriumWebhookSubscriptionResponse createSubscription(String accessToken, MoneriumWebhookSubscriptionRequest request);
}
