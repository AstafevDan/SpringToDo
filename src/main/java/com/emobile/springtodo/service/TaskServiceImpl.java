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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Реализация {@link TaskService}.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskReadMapper taskReadMapper;
    private final TaskCreateEditMapper taskCreateEditMapper;

    /**
     * Возвращает страницу задач с учётом параметров пагинации.
     * Результат кэшируется.
     *
     * @param pageable Параметры пагинации.
     * @return Страница задач в виде DTO {@link TaskReadDto}.
     */
    @Cacheable(value = "tasks_by_page", key = "{#pageable.pageNumber, #pageable.pageSize, #pageable.sort}")
    @Override
    public Page<TaskReadDto> findAllTasksByPage(Pageable pageable) {
        return taskRepository.findAllByPage(pageable)
                .map(taskReadMapper::map);
    }

    /**
     * Находит задачу по её идентификатору.
     * Результат кэшируется.
     *
     * @param id Идентификатор задачи.
     * @return DTO найденной задачи {@link TaskReadDto}.
     * @throws TaskNotFoundException если задача с указанным id не найдена.
     */
    @Cacheable(value = "tasks", key = "#id")
    @Override
    public TaskReadDto findTaskById(Long id) {
        return taskRepository.findById(id)
                .map(taskReadMapper::map)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }

    /**
     * Создаёт новую задачу на основе переданных данных.
     * Результат кэшируется и сбрасывает кэш списка задач.
     *
     * @param taskCreateEditDto Данные в виде DTO для создания задачи.
     * @return DTO созданной задачи {@link TaskReadDto}.
     * @throws TaskCreationException если создание задачи не удалось.
     */
    @Transactional
    @Caching(
            put = @CachePut(value = "tasks", key = "#result.id"),
            evict = @CacheEvict(value = "tasks_by_page", allEntries = true)
    )
    @Override
    public TaskReadDto createTask(TaskCreateEditDto taskCreateEditDto) {
        return Optional.of(taskCreateEditDto)
                .map(taskCreateEditMapper::map)
                .map(taskRepository::save)
                .map(taskReadMapper::map)
                .orElseThrow(() -> new TaskCreationException("Task creation failed"));
    }

    /**
     * Обновляет существующую задачу по её идентификатору.
     * Результат кэшируется.
     *
     * @param id                Идентификатор задачи для обновления.
     * @param taskCreateEditDto Данные в виде DTO для обновления задачи.
     * @return DTO обновлённой задачи {@link TaskReadDto}.
     * @throws TaskNotFoundException если задача с указанным id не найдена.
     * @throws TaskUpdateException   если обновление задачи не удалось.
     */
    @Transactional
    @CachePut(value = "tasks", key = "#id")
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

    /**
     * Удаляет задачу по её идентификатору.
     * Сбрасывает кэш задачи и списка задач.
     *
     * @param id Идентификатор задачи для удаления.
     * @return {@code true}, если задача была успешно удалена, иначе {@code false}.
     * @throws TaskNotFoundException если задача с указанным id не найдена.
     */
    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "tasks", key = "#id"),
                    @CacheEvict(value = "tasks_by_page", allEntries = true)
            }
    )
    @Override
    public boolean deleteTask(Long id) {
        return taskRepository.findById(id)
                .map(taskRepository::delete)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }

    /**
     * Обновляет статус задачи по её идентификатору.
     * Результат кэшируется.
     *
     * @param id     Идентификатор задачи.
     * @param status DTO с новым статусом задачи.
     * @return DTO обновлённой задачи {@link TaskReadDto}.
     * @throws TaskNotFoundException если задача с указанным id не найдена.
     * @throws TaskUpdateException   если обновление статуса не удалось.
     */
    @Transactional
    @CachePut(value = "tasks", key = "#id")
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

    /**
     * Обновляет приоритет задачи по её идентификатору.
     * Результат кэшируется.
     *
     * @param id       Идентификатор задачи.
     * @param priority DTO с новым приоритетом задачи.
     * @return DTO обновлённой задачи {@link TaskReadDto}.
     * @throws TaskNotFoundException если задача с указанным id не найдена.
     * @throws TaskUpdateException   если обновление приоритета не удалось.
     */
    @Transactional
    @CachePut(value = "tasks", key = "#id")
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
