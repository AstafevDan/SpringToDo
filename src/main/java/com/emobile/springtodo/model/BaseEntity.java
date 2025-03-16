package com.emobile.springtodo.model;

import java.io.Serializable;

/**
 * Базовый интерфейс для всех сущностей, использующих Id.
 *
 * @param <T> Тип идентификатора, реализующий {@link Serializable}.
 */
public interface BaseEntity<T extends Serializable> {

    /**
     * Возвращает идентификатор сущности.
     *
     * @return Идентификатор сущности.
     */
    T getId();

    /**
     * Устанавливает идентификатор сущности.
     *
     * @param id Идентификатор сущности.
     */
    void setId(T id);
}
