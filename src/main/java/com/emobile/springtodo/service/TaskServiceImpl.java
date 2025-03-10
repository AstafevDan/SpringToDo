package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.TaskCreateEditDto;
import com.emobile.springtodo.dto.TaskReadDto;
import com.emobile.springtodo.dto.UpdatePriorityDto;
import com.emobile.springtodo.dto.UpdateStatusDto;
import com.emobile.springtodo.exception.TaskCreationException;
import com.emobile.springtodo.exception.TaskNotFoundException;
import com.emobile.springtodo.exception.TaskUpdateException;
import com.emobile.springtodo.mapper.TaskCreateEditMapper;
import com.emobile.springtodo.mapper.TaskReadMapper;
import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskReadMapper taskReadMapper;
    private final TaskCreateEditMapper taskCreateEditMapper;

    @Override
    public Page<TaskReadDto> findAllTasksByPage(Pageable pageable) {
        return taskRepository.findAllByPage(pageable)
                .map(taskReadMapper::map);
    }

    @Override
    public TaskReadDto findTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskReadMapper::map)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }

    @Transactional
    @Override
    public TaskReadDto createTask(TaskCreateEditDto taskCreateEditDto) {
        return Optional.of(taskCreateEditDto)
                .map(taskCreateEditMapper::map)
                .map(taskRepository::save)
                .map(taskReadMapper::map)
                .orElseThrow(() -> new TaskCreationException("Task creation failed"));
    }

    @Transactional
    @Override
    public TaskReadDto updateTask(Long id, TaskCreateEditDto taskCreateEditDto) {
        return taskRepository.findById(id)
                .map(task -> {
                    Task updatedTask = taskCreateEditMapper.map(taskCreateEditDto, task);
                    return taskRepository.update(updatedTask)
                            .map(taskReadMapper::map)
                            .orElseThrow(() -> new TaskUpdateException("Task with id " + id + " failed to update"));
                })
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }

    @Transactional
    @Override
    public boolean deleteTask(Long id) {
        return taskRepository.findById(id)
                .map(taskRepository::delete)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }

    @Transactional
    @Override
    public TaskReadDto updateTaskStatus(Long id, UpdateStatusDto status) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setStatus(status.getStatus());
                    return taskRepository.updateTaskStatus(task)
                            .map(taskReadMapper::map)
                            .orElseThrow(() -> new TaskUpdateException("Task with id " + id + " failed to update"));
                })
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }

    @Transactional
    @Override
    public TaskReadDto updateTaskPriority(Long id, UpdatePriorityDto priority) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setPriority(priority.getPriority());
                    return taskRepository.updateTaskPriority(task)
                            .map(taskReadMapper::map)
                            .orElseThrow(() -> new TaskUpdateException("Task with id " + id + " failed to update"));
                })
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }
}
