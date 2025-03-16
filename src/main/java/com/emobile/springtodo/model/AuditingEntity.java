package com.emobile.springtodo.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.Instant;

/**
 * Абстрактный класс, представляющий сущность с аудит-полями.
 *
 * @param <T> Тип идентификатора, реализующий {@link Serializable}.
 */
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class AuditingEntity<T extends Serializable> implements BaseEntity<T> {

    /**
     * Дата и время создания сущности.
     */
    @CreatedDate
    @Column("created_at")
    private Instant createdAt;

    /**
     * Дата и время последнего обновления сущности.
     */
    @LastModifiedDate
    @Column("last_modified_at")
    private Instant lastModifiedAt;
}
