package com.jbqneto.monerium_api.monerium.dto.response;

import java.util.List;
import java.util.UUID;

public record MoneriumAuthContextResponse(
    UUID userId,
    UUID defaultProfile,
    List<MoneriumProfileSummaryResponse> profiles
) {}
