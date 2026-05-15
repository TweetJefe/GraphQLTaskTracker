package com.tasktrackergraphql.telegram;

import com.tasktrackergraphql.user.repository.UserRepository;
import com.tasktrackergraphql.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class TelegramService extends TelegramLongPollingBot {

    private final String botToken;
    private final String botName;
    private final UserService userService;
    private final UserRepository userRepository;

    public TelegramService(
            @Value("${telegram.bot.token}") String botToken,
            @Value("${telegram.bot.name}") String botName,
            @Lazy UserService userService,
            UserRepository userRepository) {
        super(botToken);
        this.botToken = botToken;
        this.botName = botName;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();
            String languageCode = update.getMessage().getFrom().getLanguageCode();

            if (messageText.equals("/start")) {
                log.info("Received /start from user: {} (chatId: {})", username, chatId);
                userRepository.findByTelegramId(chatId).ifPresentOrElse(
                        user -> sendMessage(chatId, "Вы уже зарегистрированы, перейти к проектам:  https://t.me/taskenfurerbot/app"),
                        () -> {
                            userService.loginOrRegister(chatId, username, languageCode);
                            sendMessage(chatId, "Добро пожаловать в Task Tracker! Вы успешно зарегистрированы.");
                        }
                );
            }
        }
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build();
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message to telegram: {}", e.getMessage());
        }
    }

    public void sendTaskNotification(Long telegramId, String taskName) {
        String text = String.format("🔔 Вас назначили на новую задачу: \"%s\"", taskName);
        sendMessage(telegramId, text);
    }
}
