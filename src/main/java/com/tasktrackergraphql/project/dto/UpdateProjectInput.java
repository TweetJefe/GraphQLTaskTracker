package com.tasktrackergraphql.project.dto;

import com.tasktrackergraphql.project.enums.ProjectStatus;

public record UpdateProjectInput(
        String name,
        String description,
        ProjectStatus status
) {
}
