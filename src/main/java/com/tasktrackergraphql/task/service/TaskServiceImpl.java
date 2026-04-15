package com.tasktrackergraphql.task.service;

import com.tasktrackergraphql.task.dto.CreateTaskInput;
import com.tasktrackergraphql.task.dto.UpdateTaskInput;
import com.tasktrackergraphql.task.enums.TaskStatus;
import com.tasktrackergraphql.exceptions.NullableViolation;
import com.tasktrackergraphql.exceptions.ServerException;
import com.tasktrackergraphql.exceptions.UniquenessViolation;
import com.tasktrackergraphql.task.mapper.TaskMapper;
import com.tasktrackergraphql.task.model.TaskEntity;
import com.tasktrackergraphql.task.dto.TaskResponse;
import com.tasktrackergraphql.task.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final TaskMapper mapper;
    private final String PostgreSQLUniquenessViolation = "23505";
    private final String PostgreSQLNullableViolation = "23502";

    @Override
    public TaskResponse createTask(CreateTaskInput dto) {
        TaskEntity entity = mapper.toEntity(dto);
        saveTask(entity);
        return mapper.taskToResponse(entity);
    }

    @Override
    public TaskResponse updateTask(UpdateTaskInput dto, Long id) {
        TaskEntity task = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));

        task.setName(dto.name());
        task.setDescription(dto.description());
        task.setPriority(dto.priority());
        task.setDeadLine(dto.deadLine());
        saveTask(task);

        return mapper.taskToResponse(task);
    }

    @Override
    public void deleteTask(Long id) {
        repository.findById(id).ifPresentOrElse(
                task -> repository.deleteById(id),
                () -> {
                    throw new EntityNotFoundException("Entity Not Found!!!");
                }
        );
    }

    @Override
    public TaskEntity saveTask(TaskEntity task) {
        try{
            return repository.save(task);
        }catch (DataIntegrityViolationException exception) {
            Throwable cause = exception.getCause();
            if(cause instanceof ConstraintViolationException cve) {
                String sqlState = cve.getSQLState();
                if(sqlState.equals(PostgreSQLUniquenessViolation)) {
                    String constraintName = cve.getConstraintName();
                    throw new UniquenessViolation(constraintName);
                }else if (sqlState.equals(PostgreSQLNullableViolation)) {
                    throw new NullableViolation("Field cannot be null");
                }
            }
            throw new ServerException("Database error occurred");
        }
    }

    @Override
    public TaskResponse assignUser(Long taskId, Long assigneeId) {
        TaskEntity task = repository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(""));

        task.setAssigneeId(assigneeId);
        TaskEntity savedTask = saveTask(task);
        return mapper.taskToResponse(savedTask);
    }

    @Override
    public TaskResponse changeStatus(Long taskId, TaskStatus status) {
        TaskEntity task = repository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("EntityNotFound"));

        if (status == TaskStatus.DONE && task.getAssigneeId() == null){
            throw new IllegalStateException("Task can't be done without an assignee");
        }

        task.setStatus(status);

        TaskEntity savedTask = saveTask(task);

        return mapper.taskToResponse(savedTask);
    }

    @Override
    public TaskResponse getTaskById(Long taskId) {
        TaskEntity task = repository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(""));
        return mapper.taskToResponse(task);
    }

    @Override
    public Window<TaskResponse> getTasksByProjectId(Long projectId, ScrollPosition pos, int limit) {
        Window<TaskEntity> taskWin = repository.findByProjectId(
                projectId,
                pos,
                Limit.of(limit)
        );
        return taskWin.map(mapper::taskToResponse);
    }

    @Override
    public Window<TaskResponse> getTasksByAssigneeId(Long assigneeId, ScrollPosition pos, int limit) {
        Window<TaskEntity> taskWin = repository.findByAssigneeId(
                assigneeId,
                pos,
                Limit.of(limit)
        );
        return taskWin.map(mapper::taskToResponse);
    }

    @Override
    public void assignToProject(Long taskId, Long projectId) {
        TaskEntity task = repository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Задача не найдена"));

        task.setProjectId(projectId);
        saveTask(task);
    }

    @Override
    public void removeFromProject(Long taskId, Long projectId) {
        TaskEntity task = repository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Задача не найдена"));
        if (projectId.equals(task.getProjectId())) {
            task.setProjectId(null);
            saveTask(task);
        }
    }

    @Override
    public Window<TaskResponse> getAllTasks(Long reporterId, ScrollPosition pos, int limit) {
        Window<TaskEntity> taskWin = repository.findByReporterId(
                reporterId,
                pos,
                Limit.of(limit));
        return taskWin.map(mapper::taskToResponse);
    }

    @Override
    public void deleteAllByProjectId(Long projectId) {
        repository.deleteAllByProjectId(projectId);
    }
}
