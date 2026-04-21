package com.tasktrackergraphql.task.dto;

import com.tasktrackergraphql.task.enums.TaskPriority;

import java.time.OffsetDateTime;

public record CreateTaskInput(
        String name,
        String description,
        Long projectId,
        Long reporterId,
        TaskPriority priority,
        OffsetDateTime deadLine
) {}
