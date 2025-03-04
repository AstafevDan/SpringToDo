package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TaskRepository {

    Page<Task> findAllByPage(Pageable pageable);

    Optional<Task> findById(Long id);

    Task save(Task task);

    Optional<Task> update(Task task);

    boolean delete(Task task);

    Optional<Task> updateTaskPriority(Task task);

    Optional<Task> updateTaskStatus(Task task);
}
