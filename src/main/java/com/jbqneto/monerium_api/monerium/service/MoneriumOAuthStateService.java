package com.jbqneto.monerium_api.monerium.service;

import com.jbqneto.monerium_api.monerium.config.MoneriumProperties;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumOAuthStartResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MoneriumOAuthStateService {

    private static final Duration STATE_TTL = Duration.ofMinutes(10);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final Map<String, MoneriumPendingOAuthState> pendingStates = new ConcurrentHashMap<>();
    private final MoneriumProperties properties;

    public MoneriumOAuthStateService(MoneriumProperties properties) {
        this.properties = properties;
    }

    public MoneriumOAuthStartResponse createAuthorizationRequest() {
        String state = randomUrlSafeValue(32);
        String codeVerifier = randomUrlSafeValue(64);
        String codeChallenge = pkceChallenge(codeVerifier);

        pendingStates.put(state, new MoneriumPendingOAuthState(codeVerifier, Instant.now()));

        String authorizationUrl = UriComponentsBuilder.fromUriString("")
            .path("/auth")
            .queryParam("client_id", properties.authorizationClientId())
            .queryParam("redirect_uri", properties.redirectUri())
            .queryParam("response_type", "code")
            .queryParam("code_challenge_method", "S256")
            .queryParam("code_challenge", codeChallenge)
            .queryParam("state", state)
            .build()
            .toUriString();

        return new MoneriumOAuthStartResponse(state, properties.webUrl(), authorizationUrl);
    }

    public MoneriumPendingOAuthState consume(String state) {
        clearExpiredStates();

        MoneriumPendingOAuthState pendingState = pendingStates.remove(state);
        if (pendingState == null) {
            throw new IllegalArgumentException("Invalid or expired Monerium OAuth state");
        }

        return pendingState;
    }

    private void clearExpiredStates() {
        Instant now = Instant.now();
        pendingStates.entrySet().removeIf(entry -> STATE_TTL.compareTo(
            Duration.between(entry.getValue().createdAt(), now)
        ) < 0);
    }

    /**
     * PKCE requires a high-entropy verifier and a SHA-256-based challenge so the
     * callback code cannot be reused without the original verifier.
     */
    private String pkceChallenge(String codeVerifier) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is required for Monerium PKCE flow", exception);
        }
    }

    private String randomUrlSafeValue(int bytes) {
        byte[] randomBytes = new byte[bytes];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
