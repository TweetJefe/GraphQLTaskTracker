package com.tasktrackergraphql.project.model;

import com.tasktrackergraphql.project.enums.ProjectStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.time.OffsetDateTime;
import java.util.List;
@Entity
@Table(name = "projects")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ElementCollection
    @CollectionTable(name = "project_assignees", joinColumns = @JoinColumn(name = "project_id"))
    @Column(name  = "user_id")
    private List<Long> assignees = new ArrayList<>();

    @Column(nullable = false)
    private Long reporterId;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @CreatedDate
    @Column(updatable = false)
    private OffsetDateTime startedAt;

    @LastModifiedDate
    private OffsetDateTime updatedAt;

    @Column(name = "pin_code_hash")
    private String pinCodeHash;

}
