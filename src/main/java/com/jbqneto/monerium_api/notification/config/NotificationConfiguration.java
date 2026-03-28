package com.jbqneto.monerium_api.notification.config;

import com.jbqneto.monerium_api.config.TelegramProperties;
import com.jbqneto.monerium_api.notification.client.NoOpNotificationSender;
import com.jbqneto.monerium_api.notification.client.NotificationSender;
import com.jbqneto.monerium_api.notification.client.TelegramNotificationSender;
import java.time.Duration;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

@Configuration
public class NotificationConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "app.telegram", name = "enabled", havingValue = "true")
    NotificationSender telegramNotificationSender(TelegramClient telegramClient, TelegramProperties telegramProperties) {
        return new TelegramNotificationSender(telegramClient, telegramProperties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.telegram", name = "enabled", havingValue = "true")
    TelegramClient telegramClient(TelegramProperties telegramProperties) {
        if (!StringUtils.hasText(telegramProperties.botToken())) {
            throw new IllegalStateException("app.telegram.bot-token must be configured when Telegram notifications are enabled.");
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(Duration.ofSeconds(telegramProperties.connectTimeoutSeconds()))
            .readTimeout(Duration.ofSeconds(telegramProperties.readTimeoutSeconds()))
            .build();

        return new OkHttpTelegramClient(okHttpClient, telegramProperties.botToken());
    }

    @Bean
    @ConditionalOnMissingBean(NotificationSender.class)
    NotificationSender noOpNotificationSender() {
        return new NoOpNotificationSender();
    }
}
