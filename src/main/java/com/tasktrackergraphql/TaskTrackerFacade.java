package com.tasktrackergraphql;

import com.tasktrackergraphql.telegram.TelegramService;
import com.tasktrackergraphql.project.model.ProjectEntity;
import com.tasktrackergraphql.project.service.ProjectService;
import com.tasktrackergraphql.task.dto.TaskResponse;
import com.tasktrackergraphql.task.service.TaskService;
import com.tasktrackergraphql.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskTrackerFacade {
    private final TaskService taskService;
    private final ProjectService projectService;
    private final UserService userService;
    private final TelegramService telegramService;

    @Transactional
    public Long deleteProject(Long projectId) {
        taskService.deleteAllByProjectId(projectId);
        projectService.deleteProject(projectId);
        return projectId;
    }

    public Window<TaskResponse> getTasksForProject(Long projectId, ScrollPosition pos, int limit) {
        return taskService.getTasksByProjectId(projectId, pos, limit);
    }

    @Transactional
    public Long deleteUser(Long reporterId) {
        List<ProjectEntity> projects = projectService.getAllProjectByReporterId(reporterId);
        for (ProjectEntity project : projects) {
            deleteProject(project.getId());
        }
        userService.deleteUser(reporterId);
        return reporterId;
    }

    @Transactional
    public TaskResponse assignUser(Long taskId, Long assigneeId) {
        TaskResponse task = taskService.assignUser(taskId, assigneeId);
        sendNotification(task, assigneeId);
        return task;
    }

    @Transactional
    public TaskResponse assignUserByUsername(Long taskId, String username) {
        var user = userService.getUserByUsername(username);
        TaskResponse task = taskService.assignUser(taskId, user.id());
        sendNotification(task, user.id());
        return task;
    }

    private void sendNotification(TaskResponse task, Long assigneeId) {
        try {
            var user = userService.getUserById(assigneeId);
            if (user.telegramId() != null) {
                telegramService.sendTaskNotification(user.telegramId(), task.name());
            }
        } catch (Exception e) {
            log.error("Failed to send telegram notification for task {}: {}", task.id(), e.getMessage());
        }
    }
}
