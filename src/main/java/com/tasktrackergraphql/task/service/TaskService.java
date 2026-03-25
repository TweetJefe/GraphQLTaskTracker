package com.tasktrackergraphql.task.service;

import com.tasktrackergraphql.task.dto.CreateTaskInput;
import com.tasktrackergraphql.task.dto.UpdateTaskInput;
import com.tasktrackergraphql.task.enums.TaskStatus;
import com.tasktrackergraphql.task.model.TaskEntity;
import com.tasktrackergraphql.task.dto.TaskResponse;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(CreateTaskInput dto);

    TaskResponse updateTask(UpdateTaskInput dto, Long id);

    void deleteTask(Long id);

    TaskEntity saveTask(TaskEntity task);

    TaskResponse assignUser (Long taskId, Long assigneeId);

    TaskResponse changeStatus(Long taskId, TaskStatus status);

    TaskResponse getTaskById(Long taskId);

    Window<TaskResponse> getTasksByProjectId(Long projectId, ScrollPosition pos, int limit);

    Window<TaskResponse> getTasksByAssigneeId(Long assigneeId, ScrollPosition pos, int limit);

    void assignToProject(Long taskId, Long projectId);

    void removeFromProject(Long taskId, Long projectId);

    Window<TaskResponse> getAllTasks (Long userId, ScrollPosition pos, int limit);

    void deleteAllByProjectId(Long projectId);
}
