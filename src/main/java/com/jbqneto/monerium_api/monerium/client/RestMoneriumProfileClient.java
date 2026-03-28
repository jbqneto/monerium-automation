package com.jbqneto.monerium_api.monerium.client;

import com.jbqneto.monerium_api.monerium.dto.response.MoneriumProfileResponse;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestMoneriumProfileClient implements MoneriumProfileClient {

    private final RestClient moneriumRestClient;

    public RestMoneriumProfileClient(RestClient moneriumClientCredentials) {
        this.moneriumRestClient = moneriumClientCredentials;
    }

    @Override
    public MoneriumProfileResponse getProfile(String accessToken, UUID profileId) {
        return moneriumRestClient.get()
            .uri("/profiles/{profileId}", profileId)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .body(MoneriumProfileResponse.class);
    }
}
