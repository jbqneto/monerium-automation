package com.jbqneto.monerium_api.monerium.service;

import com.jbqneto.monerium_api.monerium.client.MoneriumAuthClient;
import com.jbqneto.monerium_api.monerium.client.MoneriumProfileClient;
import com.jbqneto.monerium_api.monerium.config.MoneriumProperties;
import com.jbqneto.monerium_api.monerium.dto.request.AuthorizationCodeTokenRequest;
import com.jbqneto.monerium_api.monerium.dto.request.ClientCredentialsTokenRequest;
import com.jbqneto.monerium_api.monerium.dto.request.RefreshTokenRequest;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumAuthContextResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumOAuthStartResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumProfileResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumTokenResponse;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class DefaultMoneriumAuthenticationService implements MoneriumAuthenticationService {

    private final MoneriumAuthClient moneriumAuthClient;
    private final MoneriumProfileClient moneriumProfileClient;
    private final MoneriumProperties properties;

    public DefaultMoneriumAuthenticationService(
        MoneriumAuthClient moneriumAuthClient,
        MoneriumProfileClient moneriumProfileClient,
        MoneriumProperties properties
    ) {
        this.moneriumAuthClient = moneriumAuthClient;
        this.moneriumProfileClient = moneriumProfileClient;
        this.properties = properties;
    }

    @Override
    public MoneriumTokenResponse exchangeAuthorizationCode(String code, String codeVerifier) {
        AuthorizationCodeTokenRequest request = new AuthorizationCodeTokenRequest(
            properties.authorizationClientId(),
            code,
            properties.redirectUri(),
            codeVerifier
        );

        return moneriumAuthClient.exchangeAuthorizationCode(request);
    }

    @Override
    public MoneriumTokenResponse getClientCredentialsToken() {
        ClientCredentialsTokenRequest request = new ClientCredentialsTokenRequest(
            properties.clientCredentialsClientId(),
            properties.clientSecret()
        );

        return moneriumAuthClient.getClientCredentialsToken(request);
    }

    @Override
    public MoneriumTokenResponse refreshAccessToken(String refreshToken) {
        RefreshTokenRequest request = new RefreshTokenRequest(
            properties.clientCredentialsClientId(),
            refreshToken
        );

        return moneriumAuthClient.refreshAccessToken(request);
    }

    @Override
    public MoneriumAuthContextResponse getAuthContext(String accessToken) {
        return moneriumAuthClient.getAuthContext(accessToken);
    }

    @Override
    public MoneriumProfileResponse getProfile(String accessToken, UUID profileId) {
        return moneriumProfileClient.getProfile(accessToken, profileId);
    }

    @Override
    public String authorize(MoneriumOAuthStartResponse authRequest) {

        return moneriumAuthClient.authrorize(authRequest.authorizationUrl());
    }
}
