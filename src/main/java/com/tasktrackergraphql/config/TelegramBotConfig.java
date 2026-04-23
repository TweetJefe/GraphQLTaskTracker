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
