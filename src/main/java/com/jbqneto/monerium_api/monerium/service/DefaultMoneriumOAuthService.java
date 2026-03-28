package com.jbqneto.monerium_api.monerium.service;

import com.jbqneto.monerium_api.monerium.dto.response.MoneriumAccountSummaryResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumAuthContextResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumOAuthCallbackResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumOAuthStartResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumProfileResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumProfileSummaryResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumTokenResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class DefaultMoneriumOAuthService implements MoneriumOAuthService {

    private final MoneriumAuthenticationService moneriumAuthenticationService;
    private final MoneriumOAuthStateService moneriumOAuthStateService;
    private final MoneriumProfileSnapshotService moneriumProfileSnapshotService;

    public DefaultMoneriumOAuthService(
        MoneriumAuthenticationService moneriumAuthenticationService,
        MoneriumOAuthStateService moneriumOAuthStateService,
        MoneriumProfileSnapshotService moneriumProfileSnapshotService
    ) {
        this.moneriumAuthenticationService = moneriumAuthenticationService;
        this.moneriumOAuthStateService = moneriumOAuthStateService;
        this.moneriumProfileSnapshotService = moneriumProfileSnapshotService;
    }

    @Override
    public MoneriumOAuthStartResponse startAuthorization() {
        var authRequest = moneriumOAuthStateService.createAuthorizationRequest();

        String result = moneriumAuthenticationService.authorize(authRequest);

        return authRequest;
    }

    @Override
    public MoneriumOAuthCallbackResponse handleCallback(String code, String state) {
        MoneriumPendingOAuthState pendingState = moneriumOAuthStateService.consume(state);
        MoneriumTokenResponse tokenResponse = moneriumAuthenticationService.exchangeAuthorizationCode(
            code,
            pendingState.codeVerifier()
        );
        MoneriumAuthContextResponse authContext = moneriumAuthenticationService.getAuthContext(
            tokenResponse.accessToken()
        );
        MoneriumProfileSummaryResponse selectedProfileSummary = resolveProfileSummary(authContext);
        MoneriumProfileResponse profile = moneriumAuthenticationService.getProfile(
            tokenResponse.accessToken(),
            selectedProfileSummary.id()
        );

        moneriumProfileSnapshotService.saveSnapshot(authContext.userId(), selectedProfileSummary, profile);

        List<MoneriumAccountSummaryResponse> accounts = profile.accounts().stream()
            .map(account -> new MoneriumAccountSummaryResponse(
                account.id(),
                account.currency(),
                account.chain(),
                account.iban(),
                account.address()
            ))
            .toList();

        return new MoneriumOAuthCallbackResponse(
            authContext.userId(),
            profile.id(),
            profile.name(),
            accounts
        );
    }

    private MoneriumProfileSummaryResponse resolveProfileSummary(MoneriumAuthContextResponse authContext) {
        UUID defaultProfileId = authContext.defaultProfile();

        return authContext.profiles().stream()
            .filter(profile -> defaultProfileId == null || defaultProfileId.equals(profile.id()))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No Monerium profile available for the authenticated user"));
    }
}
