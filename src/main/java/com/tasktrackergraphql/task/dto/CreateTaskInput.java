package com.tasktrackergraphql.task.dto;

import com.tasktrackergraphql.task.enums.TaskPriority;
import com.tasktrackergraphql.task.enums.TaskType;

public record CreateTaskInput(
        String name,
        String description,
        Long projectId,
        TaskPriority priority,
        TaskType type
) {}
