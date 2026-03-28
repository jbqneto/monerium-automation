package com.jbqneto.monerium_api.notification.service;

import com.jbqneto.monerium_api.notification.client.NotificationSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultNotificationService implements NotificationService {

    private final NotificationSender notificationSender;

    @Override
    public void sendPersonalUpdate(String message) {
        notificationSender.send(message);
    }
}
