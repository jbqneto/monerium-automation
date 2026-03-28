package com.jbqneto.monerium_api.monerium.dto.response;

import java.util.List;
import java.util.UUID;

public record MoneriumOAuthCallbackResponse(
    UUID userId,
    UUID profileId,
    String profileName,
    List<MoneriumAccountSummaryResponse> accounts
) {}
