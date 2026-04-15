package com.tasktrackergraphql.project.repository;

import com.tasktrackergraphql.project.model.ProjectEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Window<ProjectEntity> findBy( ScrollPosition pos, Limit limit);

    List<ProjectEntity> findAllByReporterId(Long reporterId);

    Window<ProjectEntity> findAll(ScrollPosition pos, Limit of);
}
