package com.jbqneto.monerium_api.notification.telegram;

import com.jbqneto.monerium_api.config.TelegramProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app.telegram", name = "enabled", havingValue = "true")
public class TelegramPersonalBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    private final TelegramProperties telegramProperties;

    @Override
    public String getBotToken() {
        return telegramProperties.botToken();
    }

    @Override
    public LongPollingSingleThreadUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String text = update.getMessage().getText();
        String chatId = String.valueOf(update.getMessage().getChatId());

        if (!"/start".equals(text) && !"/chatid".equals(text)) {
            return;
        }

        log.info("Telegram bootstrap message received from chatId={}", chatId);

        String configuredChatId = telegramProperties.chatId();
        String reply = buildReply(chatId, configuredChatId);

        try {
            telegramClient.execute(SendMessage.builder().chatId(chatId).text(reply).build());
        } catch (TelegramApiException exception) {
            log.error("Failed to reply to Telegram bootstrap command for chatId={}", chatId, exception);
        }
    }

    private String buildReply(String incomingChatId, String configuredChatId) {
        if (!StringUtils.hasText(configuredChatId)) {
            return """
                Telegram notifications are enabled, but no personal chat is configured yet.
                Use this chat id in TELEGRAM_CHAT_ID:
                %s
                """.formatted(incomingChatId).trim();
        }

        if (configuredChatId.equals(incomingChatId)) {
            return "This chat is already configured for personal notifications.";
        }

        return """
            This bot is configured to send notifications to a different chat.
            Current configured chat id:
            %s
            Your current chat id:
            %s
            """.formatted(configuredChatId, incomingChatId).trim();
    }
}
