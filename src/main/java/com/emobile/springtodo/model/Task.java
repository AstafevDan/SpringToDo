package com.emobile.springtodo.model;

import com.emobile.springtodo.model.enums.TaskPriority;
import com.emobile.springtodo.model.enums.TaskStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@SuperBuilder
@Table(name = "tasks")
public class Task extends AuditingEntity<Long> {

    @Id
    private Long id;

    @Column("title")
    private String title;

    @Column("description")
    private String description;

    @Column("status")
    private TaskStatus status;

    @Column("priority")
    private TaskPriority priority;

    public Task(Long id, String title, String description, TaskStatus status, TaskPriority priority, Instant createdAt, Instant lastModifiedAt) {
        super(createdAt, lastModifiedAt);
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }
}
