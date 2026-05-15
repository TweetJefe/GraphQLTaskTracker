package com.tasktrackergraphql;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.telegram.telegrambots.meta.TelegramBotsApi;

@SpringBootTest
class TaskTrackerGraphQlApplicationTests {

    @MockitoBean
    private TelegramBotsApi telegramBotsApi;

    @Test
    void contextLoads() {
    }

}
