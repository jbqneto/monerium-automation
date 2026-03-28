package com.jbqneto.monerium_api.monerium.client;

import com.jbqneto.monerium_api.monerium.dto.request.AuthorizationCodeTokenRequest;
import com.jbqneto.monerium_api.monerium.dto.request.ClientCredentialsTokenRequest;
import com.jbqneto.monerium_api.monerium.dto.request.RefreshTokenRequest;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumAuthContextResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumTokenResponse;

public interface MoneriumAuthClient {

    MoneriumTokenResponse exchangeAuthorizationCode(AuthorizationCodeTokenRequest request);

    MoneriumTokenResponse getClientCredentialsToken(ClientCredentialsTokenRequest request);

    MoneriumTokenResponse refreshAccessToken(RefreshTokenRequest request);

    MoneriumAuthContextResponse getAuthContext(String accessToken);

    String authrorize(String url);
}
