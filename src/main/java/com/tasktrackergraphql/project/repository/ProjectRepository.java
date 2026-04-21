package com.tasktrackergraphql.project.repository;

import com.tasktrackergraphql.project.model.ProjectEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
    Window<ProjectEntity> findBy(ScrollPosition pos, Limit limit);

    @Query("""
        SELECT DISTINCT p FROM ProjectEntity p
        LEFT JOIN p.assignees a
        WHERE p.reporterId = :userId 
        OR a = :userId 
        OR EXISTS (SELECT t FROM TaskEntity t WHERE t.projectId = p.id AND t.assigneeId = :userId)
    """)
    Window<ProjectEntity> findAllProjectsByReporterId(Long userId, ScrollPosition position, Limit limit);
}
