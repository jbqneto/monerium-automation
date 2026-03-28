package com.jbqneto.monerium_api.monerium.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MoneriumCounterpartResponse(
    MoneriumCounterpartIdentifierResponse identifier,
    MoneriumCounterpartDetailsResponse details
) {}
