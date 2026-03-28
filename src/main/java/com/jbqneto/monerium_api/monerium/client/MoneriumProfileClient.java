package com.jbqneto.monerium_api.monerium.client;

import com.jbqneto.monerium_api.monerium.dto.response.MoneriumProfileResponse;
import java.util.UUID;

public interface MoneriumProfileClient {

    MoneriumProfileResponse getProfile(String accessToken, UUID profileId);
}
