package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.TaskCreateEditDto;
import com.emobile.springtodo.dto.TaskReadDto;
import com.emobile.springtodo.dto.UpdatePriorityDto;
import com.emobile.springtodo.dto.UpdateStatusDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {

    Page<TaskReadDto> findAllTasksByPage(Pageable pageable);

    TaskReadDto findTaskById(Long id);

    TaskReadDto createTask(TaskCreateEditDto taskCreateEditDto);

    TaskReadDto updateTask(Long id, TaskCreateEditDto taskCreateEditDto);

    boolean deleteTask(Long id);

    TaskReadDto updateTaskStatus(Long id, UpdateStatusDto status);

    TaskReadDto updateTaskPriority(Long id, UpdatePriorityDto priority);
}
