package com.jbqneto.monerium_api.monerium.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MoneriumProfileWebhookData(
    UUID id,
    String kind,
    String name,
    String state
) {}
