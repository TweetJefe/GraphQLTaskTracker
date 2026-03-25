package com.tasktrackergraphql.project.mapper;

import com.tasktrackergraphql.project.dto.CreateProjectInput;
import com.tasktrackergraphql.project.dto.ProjectResponse;
import com.tasktrackergraphql.project.model.ProjectEntity;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    public ProjectResponse toResponse (ProjectEntity project){
        if (project == null){
            return null;
        }

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getAssignees(),
                project.getReporterId(),
                project.getStatus(),
                project.getStartedAt(),
                project.getUpdatedAt()
        );
    }

    public ProjectEntity toEntity (CreateProjectInput input){
        if(input == null){
            return null;
        }

        ProjectEntity project = new ProjectEntity();
        project.setName(input.name());
        project.setDescription(input.description());
        project.setReporterId(input.reporterId());
        project.setAssignees(input.assignees());

        return project;
    }
}
