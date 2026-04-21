package com.tasktrackergraphql.user.dto;

import java.time.OffsetDateTime;

public record UserResponse(
        Long id,
        Long telegramId,
        String username,
        String name,
        String languageCode,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
