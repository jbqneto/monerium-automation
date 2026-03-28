package com.jbqneto.monerium_api.monerium.dto.response;

import java.util.UUID;

public record MoneriumAccountResponse(
    UUID id,
    String address,
    String currency,
    String chain,
    String iban,
    String standard
) {}
