package com.tasktrackergraphql.project.service;


import com.tasktrackergraphql.project.dto.CreateProjectInput;
import com.tasktrackergraphql.project.dto.ProjectResponse;
import com.tasktrackergraphql.project.mapper.ProjectMapper;
import com.tasktrackergraphql.project.model.ProjectEntity;
import com.tasktrackergraphql.project.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {

    @Mock
    private ProjectRepository repository;

    @Mock
    private ProjectMapper mapper;

    @InjectMocks
    private ProjectServiceImpl service;

    @Test
    public void testCreateProject_Succes(){
        CreateProjectInput input = new CreateProjectInput("Test Project", "Desc", 1L, List.of());
        ProjectEntity project = new ProjectEntity();
        ProjectEntity savedProject = new ProjectEntity();
        savedProject.setId(100L);
        ProjectResponse expectedResponse = new ProjectResponse(
                100L,
                "Test Project",
                "Desc",
                List.of(),
                1L,
                null,
                null,
                null);

        Mockito.when(mapper.toEntity(input)).thenReturn(project);
        Mockito.when(repository.save(project)).thenReturn(savedProject);
        Mockito.when(mapper.toResponse(savedProject)).thenReturn(expectedResponse);

        ProjectResponse actualResponse = service.createProject(input);
    }
}
