package com.jbqneto.monerium_api.monerium.dto.response;

import java.util.UUID;

public record MoneriumAccountSummaryResponse(
    UUID id,
    String currency,
    String chain,
    String iban,
    String address
) {}
