package com.emobile.springtodo.http.controller;

import com.emobile.springtodo.dto.TaskCreateEditDto;
import com.emobile.springtodo.dto.TaskReadDto;
import com.emobile.springtodo.dto.UpdatePriorityDto;
import com.emobile.springtodo.dto.UpdateStatusDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface TaskController {

    ResponseEntity<Page<TaskReadDto>> findTasks(Pageable pageable);

    ResponseEntity<TaskReadDto> findTaskById(Long id);

    ResponseEntity<TaskReadDto> createTask(TaskCreateEditDto task);

    ResponseEntity<TaskReadDto> updateTask(Long id, TaskCreateEditDto task);

    ResponseEntity<Void> deleteTask(Long id);

    ResponseEntity<TaskReadDto> updateTaskStatus(Long id, UpdateStatusDto status);

    ResponseEntity<TaskReadDto> updateTaskPriority(Long id, UpdatePriorityDto priority);
}
