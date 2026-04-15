package com.tasktrackergraphql.task.api;

import com.tasktrackergraphql.task.dto.CreateTaskInput;
import com.tasktrackergraphql.task.dto.TaskResponse;
import com.tasktrackergraphql.task.dto.UpdateTaskInput;
import com.tasktrackergraphql.task.enums.TaskStatus;
import com.tasktrackergraphql.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TaskGraphQLController {
    private final TaskService service;

    @MutationMapping
    public TaskResponse createTask(
            @Argument CreateTaskInput input){
        return service.createTask(input);
    }

    @MutationMapping
    public TaskResponse updateTask(
            @Argument UpdateTaskInput input,
            @Argument Long id){
        return service.updateTask(input, id);
    }

    @MutationMapping
    public Long deleteTask(
            @Argument Long id){
        service.deleteTask(id);
        return id;
    }

    @MutationMapping
    public TaskResponse assignUser(
            @Argument Long taskId,
            @Argument Long assigneeId){
        return service.assignUser(taskId, assigneeId);
    }

    @MutationMapping
    public TaskResponse changeStatus(
            @Argument Long taskId,
            @Argument TaskStatus status){
        return service.changeStatus(taskId, status);
    }

    @QueryMapping
    public Window<TaskResponse> getTasksByProjectId(
            @Argument Long projectId,
            ScrollSubrange subrange){
        ScrollPosition position = subrange.position().orElse(ScrollPosition.keyset());
        int limit = subrange.count().orElse(10);
        return service.getTasksByProjectId(projectId, position, limit);
    }

    @QueryMapping
    public Window<TaskResponse> getTasksByAssigneeId(
            @Argument Long assigneeId,
            ScrollSubrange subrange){
        ScrollPosition position = subrange.position().orElse(ScrollPosition.keyset());
        int limit = subrange.count().orElse(10);
        return service.getTasksByAssigneeId(assigneeId, position, limit);
    }

    @QueryMapping
    public TaskResponse getTaskById(
            @Argument Long id
    ){
        return service.getTaskById(id);
    }

    @QueryMapping
    public Window<TaskResponse> getAllTasks(
            @Argument Long reporterId,
            ScrollSubrange subrange){
        ScrollPosition position = subrange.position().orElse(ScrollPosition.keyset());
        int limit = subrange.count().orElse(10);
        return service.getAllTasks(reporterId, position, limit);
    }
}
