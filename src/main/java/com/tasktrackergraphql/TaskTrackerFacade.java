package com.tasktrackergraphql;

import com.tasktrackergraphql.project.model.ProjectEntity;
import com.tasktrackergraphql.project.service.ProjectService;
import com.tasktrackergraphql.task.dto.TaskResponse;
import com.tasktrackergraphql.task.service.TaskService;
import com.tasktrackergraphql.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TaskTrackerFacade {
    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;


    @Transactional
    public Long deleteProject(Long projectId){

        taskService.deleteAllByProjectId(projectId);

        projectService.deleteProject(projectId);

        return projectId;
    }

    public Window<TaskResponse> getTasksForProject(Long projectId, ScrollPosition pos, int limit) {
        return taskService.getTasksByProjectId(projectId, pos, limit);
    }

    @Transactional
    public Long deleteUser(Long reporterId){

        List<ProjectEntity> projects = projectService.getAllProjectByReporterId(reporterId);

        for (ProjectEntity project : projects){
            deleteProject(project.getId());
        }

        userService.deleteUser(reporterId);

        return reporterId;
    }
}
