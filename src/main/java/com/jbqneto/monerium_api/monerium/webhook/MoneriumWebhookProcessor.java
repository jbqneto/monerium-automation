package com.jbqneto.monerium_api.monerium.webhook;

import com.jbqneto.monerium_api.monerium.dto.request.MoneriumWebhookRequest;

public interface MoneriumWebhookProcessor {

    void process(MoneriumWebhookRequest request, String signatureHeader);
}
