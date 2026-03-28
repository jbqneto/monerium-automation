package com.jbqneto.monerium_api.monerium.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MoneriumCounterpartDetailsResponse(
    String firstName,
    String lastName,
    String companyName,
    String country,
    String name
) {}
