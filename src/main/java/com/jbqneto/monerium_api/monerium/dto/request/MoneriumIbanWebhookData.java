package com.jbqneto.monerium_api.monerium.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MoneriumIbanWebhookData(
    String iban,
    String name,
    String bic,
    UUID profile,
    String address,
    String chain
) {}
