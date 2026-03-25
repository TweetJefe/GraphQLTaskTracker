package com.tasktrackergraphql.task.repository;

import com.tasktrackergraphql.task.model.TaskEntity;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    Window<TaskEntity> findByProjectId(Long projectId, ScrollPosition pos, Limit limit);

    Window<TaskEntity> findByAssigneeId(Long assigneeId, ScrollPosition pos, Limit limit);

    Window<TaskEntity> findByUserId(Long userId, ScrollPosition pos, Limit limit);

    @Modifying
    @Query("DELETE FROM TaskEntity t WHERE t.projectId = :projectId")
    void deleteAllByProjectId(@Param("projectId") Long projectId);
}
