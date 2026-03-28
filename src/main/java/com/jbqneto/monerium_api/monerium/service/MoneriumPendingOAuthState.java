package com.jbqneto.monerium_api.monerium.service;

import java.time.Instant;

public record MoneriumPendingOAuthState(
    String codeVerifier,
    Instant createdAt
) {}
