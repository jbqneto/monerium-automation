package com.jbqneto.monerium_api.monerium.dto.request;

public record AuthorizationCodeTokenRequest(
    String clientId,
    String code,
    String redirectUri,
    String codeVerifier
) {}
