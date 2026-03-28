package com.jbqneto.monerium_api.monerium.dto.internal;

import com.jbqneto.monerium_api.monerium.dto.response.MoneriumAuthContextResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumTokenResponse;

public record MoneriumAuthContext(
        MoneriumTokenResponse auth,
        MoneriumAuthContextResponse context
) {
}
