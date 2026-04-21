package com.tasktrackergraphql.task.dto;

import com.tasktrackergraphql.task.enums.TaskPriority;
import com.tasktrackergraphql.task.enums.TaskStatus;

import java.time.OffsetDateTime;

public record TaskResponse(
        Long id,
        String name,
        String description,
        TaskStatus status,
        TaskPriority priority,
        Long projectId,
        Long assigneeId,
        Long reporterId,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        OffsetDateTime deadLine)
{}
