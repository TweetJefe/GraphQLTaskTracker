package com.tasktrackergraphql.project.dto;

import java.util.List;

public record CreateProjectInput(
        String name,
        String description,
        Long reporterId,
        List<Long> assignees
) {}
