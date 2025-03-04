package com.emobile.springtodo.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public abstract class AuditingEntity<T extends Serializable> implements BaseEntity<T> {

    @CreatedDate
    @Column("created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Column("last_modified_at")
    private Instant lastModifiedAt;
}
