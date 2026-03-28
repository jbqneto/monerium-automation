package com.jbqneto.monerium_api.monerium.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record MoneriumTokenResponse(
    @JsonProperty("access_token")
    String accessToken,
    @JsonProperty("expires_in")
    Integer expiresIn,
    UUID profile,
    @JsonProperty("refresh_token")
    String refreshToken,
    @JsonProperty("token_type")
    String tokenType,
    UUID userId
) {}
