package com.jbqneto.monerium_api.notification.client;

import com.jbqneto.monerium_api.config.TelegramProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@RequiredArgsConstructor
public class TelegramNotificationSender implements NotificationSender {

    private final TelegramClient telegramClient;
    private final TelegramProperties telegramProperties;

    @Override
    public void send(String message) {
        if (!StringUtils.hasText(telegramProperties.chatId())) {
            log.warn("Telegram notification skipped because app.telegram.chat-id is not configured.");
            return;
        }

        SendMessage sendMessage = SendMessage.builder()
            .chatId(telegramProperties.chatId())
            .text(message)
            .build();

        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException exception) {
            throw new IllegalStateException("Failed to send Telegram notification.", exception);
        }
    }
}
