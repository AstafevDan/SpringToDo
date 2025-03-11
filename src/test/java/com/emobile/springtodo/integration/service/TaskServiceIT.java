package com.emobile.springtodo.integration.service;

import com.emobile.springtodo.dto.TaskCreateEditDto;
import com.emobile.springtodo.dto.TaskReadDto;
import com.emobile.springtodo.dto.UpdatePriorityDto;
import com.emobile.springtodo.dto.UpdateStatusDto;
import com.emobile.springtodo.exception.TaskNotFoundException;
import com.emobile.springtodo.integration.IntegrationTestBase;
import com.emobile.springtodo.model.enums.TaskPriority;
import com.emobile.springtodo.model.enums.TaskStatus;
import com.emobile.springtodo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class TaskServiceIT extends IntegrationTestBase {

    private final TaskService taskService;
    private final CacheManager cacheManager;

    private static final Long TASK_ID = 1L;

    @Test
    void findAllTasksByPage() {
        Page<TaskReadDto> tasks = taskService.findAllTasksByPage(PageRequest.of(0, 5));

        assertNotNull(tasks);
        assertThat(tasks).hasSize(5);
        assertThat(tasks.getTotalElements()).isEqualTo(5);
    }

    @Test
    void findTaskById() {
        TaskReadDto task = taskService.findTaskById(TASK_ID);

        assertNotNull(task);
        assertEquals(TASK_ID, task.getId());
    }

    @Test
    void createTask() {
        TaskCreateEditDto taskCreateEditDto = new TaskCreateEditDto(
                "new task",
                "new description",
                TaskStatus.PENDING,
                TaskPriority.HIGH
        );

        TaskReadDto savedTask = taskService.createTask(taskCreateEditDto);

        assertNotNull(savedTask);
        assertEquals(taskCreateEditDto.getTitle(), savedTask.getTitle());
        assertEquals(taskCreateEditDto.getDescription(), savedTask.getDescription());
        assertEquals(taskCreateEditDto.getStatus(), savedTask.getStatus());
        assertEquals(taskCreateEditDto.getPriority(), savedTask.getPriority());
    }

    @Test
    void updateTask() {
        TaskCreateEditDto taskCreateEditDto = new TaskCreateEditDto(
                "updated task",
                "updated description",
                null,
                null
        );

        TaskReadDto updatedTask = taskService.updateTask(TASK_ID, taskCreateEditDto);

        assertNotNull(updatedTask);
        assertEquals(taskCreateEditDto.getTitle(), updatedTask.getTitle());
        assertEquals(taskCreateEditDto.getDescription(), updatedTask.getDescription());
    }

    @Test
    void deleteTask() {
        assertTrue(taskService.deleteTask(TASK_ID));
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(100L));
    }

    @Test
    void updateTaskStatus() {
        UpdateStatusDto updateStatusDto = new UpdateStatusDto(TaskStatus.COMPLETED);

        TaskReadDto updatedTask = taskService.updateTaskStatus(TASK_ID, updateStatusDto);

        assertNotNull(updatedTask);
        assertEquals(TaskStatus.COMPLETED, updatedTask.getStatus());
    }

    @Test
    void updateTaskPriority() {
        UpdatePriorityDto updatePriorityDto = new UpdatePriorityDto(TaskPriority.HIGH);

        TaskReadDto updatedTask = taskService.updateTaskPriority(TASK_ID, updatePriorityDto);

        assertNotNull(updatedTask);
        assertEquals(TaskPriority.HIGH, updatedTask.getPriority());
    }

    @Test
    void shouldStoreAndRetrieveCacheValues() {
        TaskCreateEditDto taskCreateEditDto = new TaskCreateEditDto(
                "new task",
                "new description",
                TaskStatus.PENDING,
                TaskPriority.HIGH
        );
        TaskReadDto createdTask = taskService.createTask(taskCreateEditDto);

        Cache cache = cacheManager.getCache("tasks");
        assertNotNull(cache);
        assertNotNull(createdTask.getId());
        assertThat(cache.get(createdTask.getId())).isNotNull();

        taskService.deleteTask(createdTask.getId());
        assertThat(cache.get(createdTask.getId())).isNull();
    }
}
