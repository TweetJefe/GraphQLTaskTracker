package com.tasktrackergraphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.OffsetDateTime;
import java.util.Optional;

@SpringBootApplication
@EnableJpaAuditing(dateTimeProviderRef = "auditingDateTimeProvider")
public class TaskTrackerGraphQlApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerGraphQlApplication.class, args);
    }

    @Bean(name = "auditingDateTimeProvider")
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(OffsetDateTime.now());
    }

}
