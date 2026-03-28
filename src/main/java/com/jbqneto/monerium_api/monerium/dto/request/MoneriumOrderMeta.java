package com.jbqneto.monerium_api.monerium.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MoneriumOrderMeta(
    Instant placedAt,
    Instant processedAt,
    String rejectedReason,
    UUID supportingDocumentId,
    List<String> txHashes
) {}
