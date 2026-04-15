package com.tasktrackergraphql.user.dto;

import java.time.Instant;

public record UserResponse(
        Long id,
        Long telegramId,
        String username,
        String name,
        String languageCode,
        Instant createdAt,
        Instant updatedAt
) {
}
