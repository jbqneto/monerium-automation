package com.jbqneto.monerium_api.monerium.dto.response;

import java.util.List;
import java.util.UUID;

public record MoneriumProfileSummaryResponse(
    UUID id,
    String kind,
    String name,
    List<String> perms
) {}
