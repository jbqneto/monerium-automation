package com.jbqneto.monerium_api.monerium.service;

import com.jbqneto.monerium_api.monerium.dto.response.MoneriumAuthContextResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumOAuthStartResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumProfileResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumTokenResponse;
import java.util.UUID;

public interface MoneriumAuthenticationService {

    MoneriumTokenResponse exchangeAuthorizationCode(String code, String codeVerifier);

    MoneriumTokenResponse getClientCredentialsToken();

    MoneriumTokenResponse refreshAccessToken(String refreshToken);

    MoneriumAuthContextResponse getAuthContext(String accessToken);

    MoneriumProfileResponse getProfile(String accessToken, UUID profileId);

    String authorize(MoneriumOAuthStartResponse authRequest);
}
