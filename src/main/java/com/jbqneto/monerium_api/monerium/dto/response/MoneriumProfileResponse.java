package com.jbqneto.monerium_api.monerium.dto.response;

import java.util.List;
import java.util.UUID;

public record MoneriumProfileResponse(
    UUID id,
    String name,
    MoneriumKycResponse kyc,
    List<MoneriumAccountResponse> accounts
) {}
