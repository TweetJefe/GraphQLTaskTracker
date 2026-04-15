package com.tasktrackergraphql.task.dto;

import com.tasktrackergraphql.task.enums.TaskPriority;

public record CreateTaskInput(
        String name,
        String description,
        Long projectId,
        TaskPriority priority
) {}
