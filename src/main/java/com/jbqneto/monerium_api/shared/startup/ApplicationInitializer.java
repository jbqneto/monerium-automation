package com.jbqneto.monerium_api.shared.startup;

import com.jbqneto.monerium_api.monerium.dto.response.MoneriumAuthContextResponse;
import com.jbqneto.monerium_api.monerium.dto.response.MoneriumTokenResponse;
import com.jbqneto.monerium_api.monerium.service.MoneriumAuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApplicationInitializer implements ApplicationRunner {

    private final MoneriumAuthenticationService authenticationService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Initializing Monerium client credentials token...");
        MoneriumTokenResponse token = authenticationService.getClientCredentialsToken();
        MoneriumAuthContextResponse context = authenticationService.getAuthContext(token.accessToken());

        log.info("Monerium authenticated user loaded successfully.");
        log.info("User Id: {}", context.userId());
        log.info("Default profile: {}", context.defaultProfile());
        log.info("Profiles count: {}", context.profiles() != null ? context.profiles().size() : 0);
    }
}
