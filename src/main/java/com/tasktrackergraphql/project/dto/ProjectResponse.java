package com.tasktrackergraphql.project.dto;

import com.tasktrackergraphql.project.enums.ProjectStatus;
import com.tasktrackergraphql.task.dto.TaskResponse;
import java.time.OffsetDateTime;
import java.util.List;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        List<Long> assignees,
        Long reporterId,
        ProjectStatus status,
        OffsetDateTime startedAt,
        OffsetDateTime updatedAt
) {
}

