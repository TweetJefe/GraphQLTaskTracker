package com.tasktrackergraphql.config;

import com.tasktrackergraphql.telegram.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TelegramBotConfig {

    private final TelegramService telegramService;

    @Bean
    public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
        log.info("Checking Telegram bot configuration...");
        String botUsername = telegramService.getBotUsername();
        String botToken = telegramService.getBotToken();

        if (botUsername == null || botUsername.trim().isEmpty() || botUsername.contains("${") ||
            botToken == null || botToken.trim().isEmpty() || botToken.contains("${")) {
            log.warn("Telegram bot credentials are not configured or contain placeholders. Skipping bot registration.");
            return new TelegramBotsApi(DefaultBotSession.class);
        }

        log.info("Manually registering TelegramBotService...");
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        try {
            api.registerBot(telegramService);
            log.info("TelegramBotService registered successfully!");
        } catch (TelegramApiException e) {
            log.error("Failed to register telegram bot: {}", e.getMessage());
        }
        return api;
    }
}
