package com.tasktrackergraphql.project.repository;

import com.tasktrackergraphql.project.model.ProjectEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Window<ProjectEntity> findAll( ScrollPosition pos, Limit limit);
}
