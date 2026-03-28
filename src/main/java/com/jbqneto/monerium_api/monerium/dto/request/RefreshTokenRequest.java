package com.jbqneto.monerium_api.monerium.dto.request;

public record RefreshTokenRequest(
    String clientId,
    String refreshToken
) {}
