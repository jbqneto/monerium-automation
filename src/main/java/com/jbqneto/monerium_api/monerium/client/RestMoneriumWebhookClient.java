package com.jbqneto.monerium_api.monerium.client;

import com.jbqneto.monerium_api.monerium.dto.request.MoneriumWebhookSubscriptionRequest;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumWebhookSubscriptionResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumWebhookSubscriptionsResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestMoneriumWebhookClient implements MoneriumWebhookClient {

    private final RestClient moneriumRestClient;

    public RestMoneriumWebhookClient(RestClient moneriumRestClient) {
        this.moneriumRestClient = moneriumRestClient;
    }

    @Override
    public MoneriumWebhookSubscriptionsResponse listSubscriptions(String accessToken) {
        return moneriumRestClient.get()
            .uri("/webhooks")
            .header(HttpHeaders.AUTHORIZATION, bearer(accessToken))
            .retrieve()
            .body(MoneriumWebhookSubscriptionsResponse.class);
    }

    @Override
    public MoneriumWebhookSubscriptionResponse createSubscription(String accessToken, MoneriumWebhookSubscriptionRequest request) {
        return moneriumRestClient.post()
            .uri("/webhooks")
            .header(HttpHeaders.AUTHORIZATION, bearer(accessToken))
            .contentType(MediaType.APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(MoneriumWebhookSubscriptionResponse.class);
    }

    private String bearer(String accessToken) {
        return "Bearer " + accessToken;
    }
}
