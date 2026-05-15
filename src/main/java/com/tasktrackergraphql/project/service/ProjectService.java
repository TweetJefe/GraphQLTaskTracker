package com.tasktrackergraphql.project.service;

import com.tasktrackergraphql.project.dto.CreateProjectInput;
import com.tasktrackergraphql.project.dto.ProjectResponse;
import com.tasktrackergraphql.project.dto.UpdateProjectInput;
import com.tasktrackergraphql.project.model.ProjectEntity;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;

import java.util.List;

public interface ProjectService{
    ProjectResponse createProject (CreateProjectInput input);

    ProjectEntity saveProject (ProjectEntity project);

    ProjectResponse updateProject (UpdateProjectInput input, Long id);

    Long deleteProject (Long id);

    ProjectResponse addAssignee (Long projectId, Long assigneeId);

    ProjectResponse removeAssignee (Long projectId, Long assigneeId);

    ProjectResponse getProjectById (Long projectId);

    Window<ProjectResponse> getAllProjects (Long userId, ScrollPosition pos, int limit);

    List<ProjectEntity> getAllProjectByReporterId(Long reporterId);
}
