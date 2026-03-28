package com.jbqneto.monerium_api.monerium.service;

import com.jbqneto.monerium_api.monerium.client.MoneriumWebhookClient;
import com.jbqneto.monerium_api.monerium.config.MoneriumProperties;
import com.jbqneto.monerium_api.monerium.dto.MoneriumWebhookEventType;
import com.jbqneto.monerium_api.monerium.dto.internal.MoneriumAuthContext;
import com.jbqneto.monerium_api.monerium.dto.request.MoneriumWebhookSubscriptionRequest;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumAuthContextResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumTokenResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumWebhookSubscriptionResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumWebhookSubscriptionsResponse;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoneriumDefaultObserverService implements MoneriumObserverService {

    private static final List<MoneriumWebhookEventType> EXPECTED_TYPES = List.of(
        MoneriumWebhookEventType.IBAN_UPDATED,
        MoneriumWebhookEventType.ORDER_CREATED,
        MoneriumWebhookEventType.ORDER_UPDATED
    );

    private final MoneriumWebhookClient webhookClient;
    private final MoneriumProperties moneriumProperties;
    private final MoneriumAuthenticationService authenticationService;
    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public MoneriumAuthContext getInitialDataAndWatch(MoneriumProperties properties) {
        MoneriumAuthContext authResponse = getAuthContextData();

        executor.execute(() -> this.verifyListeners(authResponse.auth()));

        return authResponse;
    }

    private void verifyListeners(MoneriumTokenResponse auth) {
        String token = auth.accessToken();
        if (!StringUtils.hasText(moneriumProperties.webhookUrl())) {
            log.info("Skipping Monerium webhook synchronization because app.monerium.webhook-url is not configured.");
            return;
        }

        if (!StringUtils.hasText(moneriumProperties.webhookSecret())) {
            throw new IllegalStateException("app.monerium.webhook-secret must be configured when webhook synchronization is enabled.");
        }

        URI webhookUrl = URI.create(moneriumProperties.webhookUrl());
        MoneriumWebhookSubscriptionsResponse response = webhookClient.listSubscriptions(token);

        MoneriumWebhookSubscriptionResponse existing = response != null && response.subscriptions() != null
                ? response.subscriptions().stream()
                .filter(subscription -> matches(subscription, webhookUrl))
                .findFirst()
                .orElse(null)
                : null;

        if (existing != null) {
            log.info("Monerium webhook subscription already exists for {}.", webhookUrl);
            return;
        }

        log.info("Creating Monerium webhook subscription for {}.", webhookUrl);

        webhookClient.createSubscription(
                token,
                new MoneriumWebhookSubscriptionRequest(webhookUrl, moneriumProperties.webhookSecret(), EXPECTED_TYPES)
        );
    }

    private boolean matches(MoneriumWebhookSubscriptionResponse subscription, URI webhookUrl) {
        return subscription != null
            && subscription.url() != null
            && Objects.equals(subscription.url().toString(), webhookUrl.toString())
            && subscription.types() != null
            && subscription.types().containsAll(EXPECTED_TYPES)
            && EXPECTED_TYPES.containsAll(subscription.types());
    }

    private MoneriumAuthContext getAuthContextData() {
        log.info("Initializing Monerium client credentials token...");
        MoneriumTokenResponse token = authenticationService.getClientCredentialsToken();
        MoneriumAuthContextResponse context = authenticationService.getAuthContext(token.accessToken());

        log.info("Monerium authenticated user loaded successfully.");
        log.info("User Id: {}", context.userId());
        log.info("Default profile: {}", context.defaultProfile());
        log.info("Profiles count: {}", context.profiles() != null ? context.profiles().size() : 0);

        return new MoneriumAuthContext(
                token,
                context
        );
    }
}
