package com.jbqneto.monerium_api.monerium.webhook;

public interface MoneriumWebhookSignatureValidator {

    boolean isValid(String payload, String signatureHeader);
}
