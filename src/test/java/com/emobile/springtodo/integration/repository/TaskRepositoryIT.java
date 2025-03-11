package com.emobile.springtodo.integration.repository;

import com.emobile.springtodo.integration.IntegrationTestBase;
import com.emobile.springtodo.model.Task;
import com.emobile.springtodo.model.enums.TaskPriority;
import com.emobile.springtodo.model.enums.TaskStatus;
import com.emobile.springtodo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
public class TaskRepositoryIT extends IntegrationTestBase {

    private final TaskRepository taskRepository;

    private static final Long TASK_ID = 3L;
    private static final Long TASK_SAVE_ID = 6L;

    @Test
    void findAllTasksByPage() {
        Page<Task> tasks = taskRepository.findAllByPage(PageRequest.of(0, 2));

        assertNotNull(tasks);
        assertThat(tasks).hasSize(2);
        assertThat(tasks.getTotalElements()).isEqualTo(5);
    }

    @Test
    void findTaskById() {
        Optional<Task> maybeTask = taskRepository.findById(TASK_ID);

        assertTrue(maybeTask.isPresent());
        assertThat(maybeTask.get().getId()).isEqualTo(TASK_ID);
    }

    @Test
    void saveTask() {
        Task task = Task.builder()
                .title("Task Title")
                .description("Task Description")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.LOW)
                .build();

        Task savedTask = taskRepository.save(task);

        assertNotNull(savedTask);
        assertThat(savedTask.getId()).isEqualTo(TASK_SAVE_ID);
    }

    @Test
    void updateTask() {
        Optional<Task> maybeTask = taskRepository.findById(TASK_ID);
        assertTrue(maybeTask.isPresent());
        assertThat(maybeTask.get().getTitle()).isEqualTo("task3");
        assertThat(maybeTask.get().getDescription()).isEqualTo("description3");
        assertThat(maybeTask.get().getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(maybeTask.get().getPriority()).isEqualTo(TaskPriority.LOW);

        maybeTask.ifPresent(task -> {
            task.setTitle("Task Title Updated");
            task.setDescription("Task Description Updated");
            Optional<Task> updatedTask = taskRepository.update(task);
            assertTrue(updatedTask.isPresent());
            updatedTask.ifPresent(updatedTask1 -> assertThat(updatedTask1.getTitle()).isEqualTo("Task Title Updated"));
            updatedTask.ifPresent(updatedTask1 -> assertThat(updatedTask1.getDescription()).isEqualTo("Task Description Updated"));
        });
    }

    @Test
    void deleteTask() {
        Optional<Task> maybeTask = taskRepository.findById(TASK_ID);
        assertTrue(maybeTask.isPresent());
        maybeTask.ifPresent(task -> assertTrue(taskRepository.delete(task)));
        assertTrue(taskRepository.findById(TASK_ID).isEmpty());
    }

    @Test
    void updateTaskStatus() {
        Optional<Task> maybeTask = taskRepository.findById(TASK_ID);
        assertTrue(maybeTask.isPresent());
        assertThat(maybeTask.get().getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);

        maybeTask.ifPresent(task -> {
            task.setStatus(TaskStatus.COMPLETED);
            Optional<Task> updatedTask = taskRepository.updateTaskStatus(task);
            assertTrue(updatedTask.isPresent());
            updatedTask.ifPresent(task1 -> assertThat(task1.getStatus()).isEqualTo(TaskStatus.COMPLETED));
        });
    }

    @Test
    void updateTaskPriority() {
        Optional<Task> maybeTask = taskRepository.findById(TASK_ID);
        assertTrue(maybeTask.isPresent());
        assertThat(maybeTask.get().getPriority()).isEqualTo(TaskPriority.LOW);

        maybeTask.ifPresent(task -> {
            task.setPriority(TaskPriority.HIGH);
            Optional<Task> updatedTask = taskRepository.updateTaskPriority(task);
            assertTrue(updatedTask.isPresent());
            updatedTask.ifPresent(task1 -> assertThat(task1.getPriority()).isEqualTo(TaskPriority.HIGH));
        });
    }
}
