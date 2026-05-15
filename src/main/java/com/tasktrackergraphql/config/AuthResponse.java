package com.tasktrackergraphql.config;

public record AuthResponse(
        String token,
        Long userId
) {
}
