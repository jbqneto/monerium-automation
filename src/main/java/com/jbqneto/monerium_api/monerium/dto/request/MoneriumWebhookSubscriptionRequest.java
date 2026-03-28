package com.jbqneto.monerium_api.monerium.dto.request;

import com.jbqneto.monerium_api.monerium.dto.MoneriumWebhookEventType;
import java.net.URI;
import java.util.List;

public record MoneriumWebhookSubscriptionRequest(
    URI url,
    String secret,
    List<MoneriumWebhookEventType> types
) {}
