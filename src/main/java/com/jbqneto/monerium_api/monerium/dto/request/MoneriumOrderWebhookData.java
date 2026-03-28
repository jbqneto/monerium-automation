package com.jbqneto.monerium_api.monerium.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MoneriumOrderWebhookData(
    UUID id,
    String kind,
    UUID profile,
    String address,
    String chain,
    String currency,
    BigDecimal amount,
    MoneriumCounterpartResponse counterpart,
    String memo,
    String referenceNumber,
    String state,
    MoneriumOrderMeta meta
) {}
