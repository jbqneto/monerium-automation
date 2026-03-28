package com.jbqneto.monerium_api.monerium.client;

import com.jbqneto.monerium_api.monerium.dto.request.AuthorizationCodeTokenRequest;
import com.jbqneto.monerium_api.monerium.dto.request.ClientCredentialsTokenRequest;
import com.jbqneto.monerium_api.monerium.dto.request.RefreshTokenRequest;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumAuthContextResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumTokenResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class RestMoneriumAuthClient implements MoneriumAuthClient {

    private final RestClient moneriumAuthClient;

    public RestMoneriumAuthClient(RestClient moneriumClientAuthClient) {
        this.moneriumAuthClient = moneriumClientAuthClient;
    }

    @Override
    public MoneriumTokenResponse exchangeAuthorizationCode(AuthorizationCodeTokenRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", request.clientId());
        formData.add("code", request.code());
        formData.add("redirect_uri", request.redirectUri());
        formData.add("grant_type", "authorization_code");
        formData.add("code_verifier", request.codeVerifier());

        return tokenRequest(formData);
    }

    @Override
    public MoneriumTokenResponse getClientCredentialsToken(ClientCredentialsTokenRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", request.clientId());
        formData.add("client_secret", request.clientSecret());
        formData.add("grant_type", "client_credentials");

        return tokenRequest(formData);
    }

    @Override
    public MoneriumTokenResponse refreshAccessToken(RefreshTokenRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", request.clientId());
        formData.add("refresh_token", request.refreshToken());
        formData.add("grant_type", "refresh_token");

        return tokenRequest(formData);
    }

    @Override
    public MoneriumAuthContextResponse getAuthContext(String accessToken) {
        return moneriumAuthClient.get()
            .uri("/auth/context")
            .header(HttpHeaders.AUTHORIZATION, bearer(accessToken))
            .retrieve()
            .body(MoneriumAuthContextResponse.class);
    }

    @Override
    public String authrorize(String url) {
        return moneriumAuthClient.get()
                .uri("/auth")
                .retrieve()
                .body(String.class);
    }

    private MoneriumTokenResponse tokenRequest(MultiValueMap<String, String> formData) {
        return moneriumAuthClient.post()
                .uri("/auth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(MoneriumTokenResponse.class);
    }

    private String bearer(String accessToken) {
        return "Bearer " + accessToken;
    }
}
