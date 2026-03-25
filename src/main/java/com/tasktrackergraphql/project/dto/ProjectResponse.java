package com.tasktrackergraphql.project.dto;

import com.tasktrackergraphql.project.enums.ProjectStatus;

import java.time.Instant;
import java.util.List;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        List<Long> assignees,
        Long reporterId,
        ProjectStatus status,
        Instant startedAt,
        Instant updatedAt
){}
