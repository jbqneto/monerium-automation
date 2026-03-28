package com.jbqneto.monerium_api.monerium.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MoneriumCounterpartIdentifierResponse(
    String standard,
    String iban,
    String address,
    String chain,
    String accountNumber,
    String bic
) {}
