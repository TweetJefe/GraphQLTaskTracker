package com.tasktrackergraphql.task.mapper;

import com.tasktrackergraphql.task.dto.CreateTaskInput;
import com.tasktrackergraphql.task.enums.TaskStatus;
import com.tasktrackergraphql.task.model.TaskEntity;
import com.tasktrackergraphql.task.dto.TaskResponse;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskResponse taskToResponse(TaskEntity task){
        if (task == null){
            return null;
        }

        return new TaskResponse(
                task.getId(),
                task.getName(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getProjectId(),
                task.getAssigneeId(),
                task.getReporterId(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getDeadLine()
        );
    }

    public TaskEntity toEntity(CreateTaskInput input) {
        if (input == null) {
            return null;
        }
        TaskEntity entity = new TaskEntity();
        entity.setName(input.name());
        entity.setDescription(input.description());
        entity.setProjectId(input.projectId());
        entity.setPriority(input.priority());
        entity.setStatus(TaskStatus.TODO);

        return entity;
    }
}
