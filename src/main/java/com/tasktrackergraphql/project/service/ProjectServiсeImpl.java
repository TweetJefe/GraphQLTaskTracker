package com.tasktrackergraphql.project.service;

import com.tasktrackergraphql.exceptions.NullableViolation;
import com.tasktrackergraphql.exceptions.ServerException;
import com.tasktrackergraphql.exceptions.UniquenessViolation;
import com.tasktrackergraphql.project.dto.CreateProjectInput;
import com.tasktrackergraphql.project.dto.ProjectResponse;
import com.tasktrackergraphql.project.dto.UpdateProjectInput;
import com.tasktrackergraphql.project.mapper.ProjectMapper;
import com.tasktrackergraphql.project.model.ProjectEntity;
import com.tasktrackergraphql.project.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectServiсeImpl implements ProjectService{
    private final ProjectRepository repository;
    private final ProjectMapper mapper;

    private final String PostgreSQLUniquenessViolation = "23505";
    private final String PostgreSQLNullableViolation = "23502";


    @Override
    public ProjectResponse createProject(CreateProjectInput input) {
        ProjectEntity project = mapper.toEntity(input);
        ProjectEntity savedProject = saveProject(project);
        return mapper.toResponse(savedProject);
    }

    @Override
    public ProjectEntity saveProject(ProjectEntity project) {
        try{
            return repository.save(project);
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
    public ProjectResponse updateProject(UpdateProjectInput input, Long id) {
        ProjectEntity project = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(""));
        project.setName(input.name());
        project.setDescription(input.description());

        ProjectEntity savedProject = saveProject(project);
        return mapper.toResponse(savedProject);
    }

    @Override
    public Long deleteProject(Long id) {
        repository.findById(id).ifPresentOrElse
                (project -> repository.deleteById(id),
                        () -> {
                    throw new EntityNotFoundException("");
                });
        return id;
    }

    @Override
    public ProjectResponse addAssignee(Long projectId, Long assigneeId) {
        ProjectEntity project = repository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(""));
        if (!project.getAssignees().contains(assigneeId)) {
            project.getAssignees().add(assigneeId);
        }
        ProjectEntity savedProject = saveProject(project);

        return mapper.toResponse(savedProject);
    }

    @Override
    public ProjectResponse removeAssignee(Long projectId, Long assigneeId) {
        ProjectEntity project = repository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(""));

        project.getAssignees().remove(assigneeId);
        ProjectEntity savedProject = saveProject(project);

        return mapper.toResponse(savedProject);
    }

    @Override
    public ProjectResponse getProjectById(Long projectId) {
        ProjectEntity project = repository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException(""));
        return mapper.toResponse(project);
    }

    @Override
    public Window<ProjectResponse> getAllProjects(ScrollPosition pos, int limit) {
        Window<ProjectEntity> winProj = repository.findAll(
                pos,
                Limit.of(limit));
        return winProj.map(mapper::toResponse);
    }

}
