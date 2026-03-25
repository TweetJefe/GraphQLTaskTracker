package com.tasktrackergraphql;

import com.tasktrackergraphql.project.service.ProjectService;
import com.tasktrackergraphql.task.dto.TaskResponse;
import com.tasktrackergraphql.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TaskTrackerFacade {
    private final TaskService taskService;
    private final ProjectService projectService;


    @Transactional
    public Long deleteProject(Long projectId){

        taskService.deleteAllByProjectId(projectId);

        projectService.deleteProject(projectId);

        return projectId;
    }

    public Window<TaskResponse> getTasksForProject(Long projectId, ScrollPosition pos, int limit) {
        return taskService.getTasksByProjectId(projectId, pos, limit);
    }
}
