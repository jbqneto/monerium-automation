package com.jbqneto.monerium_api.notification.client;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoOpNotificationSender implements NotificationSender {

    @Override
    public void send(String message) {
        log.debug("Notifications are disabled. Dropping message: {}", message);
    }
}
