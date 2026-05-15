package com.tasktrackergraphql.task.dto;

import com.tasktrackergraphql.task.enums.TaskPriority;
import com.tasktrackergraphql.task.enums.TaskStatus;

import java.time.Instant;

public record UpdateTaskInput(
        String name,
        String description,
        Long projectId,
        TaskPriority priority,
        TaskStatus status,
        Instant deadLine
) {
}
