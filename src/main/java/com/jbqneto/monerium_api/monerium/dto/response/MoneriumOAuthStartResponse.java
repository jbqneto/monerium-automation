package com.jbqneto.monerium_api.monerium.dto.response;

public record MoneriumOAuthStartResponse(
    String state,
    String baseUrl,
    String authorizationUrl
) {}
