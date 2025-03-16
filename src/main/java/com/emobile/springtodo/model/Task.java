package com.emobile.springtodo.model;

import com.emobile.springtodo.model.enums.TaskPriority;
import com.emobile.springtodo.model.enums.TaskStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

/**
 * Класс, описывающий задачу.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@ToString
@SuperBuilder
@Table(name = "tasks")
public class Task extends AuditingEntity<Long> {

    /**
     * Уникальный идентификатор задачи.
     */
    @Id
    private Long id;

    /**
     * Заголовок задачи.
     */
    @Column("title")
    private String title;

    /**
     * Описание задачи.
     */
    @Column("description")
    private String description;

    /**
     * Статус задачи.
     */
    @Column("status")
    private TaskStatus status;

    /**
     * Приоритет задачи.
     */
    @Column("priority")
    private TaskPriority priority;

    /**
     * Создаёт экземпляр задачи с указанными параметрами.
     *
     * @param id             Уникальный идентификатор задачи.
     * @param title          Заголовок задачи.
     * @param description    Описание задачи.
     * @param status         Статус задачи.
     * @param priority       Приоритет задачи.
     * @param createdAt      Дата и время создания задачи.
     * @param lastModifiedAt Дата и время последнего изменения задачи.
     */
    public Task(Long id, String title, String description, TaskStatus status, TaskPriority priority, Instant createdAt, Instant lastModifiedAt) {
        super(createdAt, lastModifiedAt);
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
    }
}
