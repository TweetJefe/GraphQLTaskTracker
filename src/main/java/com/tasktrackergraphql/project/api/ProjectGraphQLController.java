package com.tasktrackergraphql.project.api;

import com.tasktrackergraphql.TaskTrackerFacade;
import com.tasktrackergraphql.project.dto.CreateProjectInput;
import com.tasktrackergraphql.project.dto.ProjectResponse;
import com.tasktrackergraphql.project.dto.UpdateProjectInput;
import com.tasktrackergraphql.project.service.ProjectService;
import com.tasktrackergraphql.task.dto.TaskResponse;
import com.tasktrackergraphql.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class ProjectGraphQLController {
    private final ProjectService service;
    private final TaskTrackerFacade facade;


    @MutationMapping
    public ProjectResponse createProject(
            @Argument CreateProjectInput input
            ){
        return service.createProject(input);
    }

    @MutationMapping
    public ProjectResponse updateProject(
            @Argument UpdateProjectInput input,
            @Argument Long id
            ){
        return service.updateProject(input, id);
    }

    @MutationMapping
    public Long deleteProject(
            @Argument Long id
            ){
        facade.deleteProject(id);
        return id;
    }

    @QueryMapping
    public ProjectResponse getProjectById(
            @Argument Long id
            ){
        return service.getProjectById(id);
    }

    @QueryMapping
    public Window<ProjectResponse> getAllProjects(
            Authentication auth,
            ScrollSubrange sub
    ){
        if (auth == null || auth.getPrincipal() == null || "anonymousUser".equals(auth.getPrincipal())) {
            throw new RuntimeException("Не авторизован! Пожалуйста, войдите в систему.");
        }
        
        Long currentUserId;
        Object principal = auth.getPrincipal();
        
        if (principal instanceof Long) {
            currentUserId = (Long) principal;
        } else if (principal instanceof String) {
            try {
                currentUserId = Long.parseLong((String) principal);
            } catch (NumberFormatException e) {
                throw new RuntimeException("Ошибка сессии: некорректный ID пользователя.");
            }
        } else if (principal instanceof UserEntity) {
            currentUserId = ((UserEntity) principal).getId();
        } else {
            throw new RuntimeException("Некорректный тип Principal: " + principal.getClass().getName());
        }


        ScrollPosition pos = sub.position().orElse(ScrollPosition.keyset());
        int limit = sub.count().orElse(10);

        return service.getAllProjects(currentUserId, pos, limit);
    }

    @MutationMapping
    public ProjectResponse addAssignee(
            @Argument Long projectId,
            @Argument Long assigneeId
    ){
        return service.addAssignee(projectId, assigneeId);
    }

    @MutationMapping
    public ProjectResponse removeAssignee(
            @Argument Long projectId,
            @Argument Long assigneeId
    ){
        return service.removeAssignee(projectId, assigneeId);
    }

    @SchemaMapping
    public Window<TaskResponse> tasks(ProjectResponse project, ScrollSubrange sub) {
        ScrollPosition pos = sub.position().orElse(ScrollPosition.keyset());
        int limit = sub.count().orElse(10);

        return facade.getTasksForProject(project.id(), pos, limit);
    }
}
