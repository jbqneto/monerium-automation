package com.jbqneto.monerium_api.shared.startup;

import com.jbqneto.monerium_api.monerium.config.MoneriumProperties;
import com.jbqneto.monerium_api.monerium.service.MoneriumObserverService;
import com.jbqneto.monerium_api.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ApplicationInitializer implements ApplicationRunner {

    private final MoneriumObserverService observerService;
    private final NotificationService notificationService;
    private final MoneriumProperties moneriumProperties;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!StringUtils.hasText(moneriumProperties.clientSecret())
            || !StringUtils.hasText(moneriumProperties.clientCredentialsClientId())) {
            log.info("Skipping Monerium startup bootstrap because client credentials are not configured.");
            return;
        }

        var authContext = observerService.getInitialDataAndWatch(moneriumProperties);

        notificationService.sendPersonalUpdate("""
            Monerium API started successfully.
            User Id: %s
            Default profile: %s
            Profiles count: %d
            """.formatted(
            authContext.context().userId(),
            authContext.context().defaultProfile(),
            authContext.context().profiles() != null ? authContext.context().profiles().size() : 0
        ).trim());
    }
}
