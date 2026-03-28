package com.jbqneto.monerium_api.monerium.dto.request;

public record ClientCredentialsTokenRequest(
    String clientId,
    String clientSecret
) {}
