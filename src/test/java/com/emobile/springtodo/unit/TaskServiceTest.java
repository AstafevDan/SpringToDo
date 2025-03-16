package com.emobile.springtodo.unit;

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
import com.emobile.springtodo.model.enums.TaskPriority;
import com.emobile.springtodo.model.enums.TaskStatus;
import com.emobile.springtodo.repository.TaskRepository;
import com.emobile.springtodo.service.TaskServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    private static final Long TASK_ID = 1L;

    private static Task task1;
    private static Task updatedTask1;
    private static Task updatedStatusTask1;
    private static Task updatedPriorityTask1;
    private static Task task2;
    private static TaskReadDto expectedTaskReadDto1;
    private static TaskReadDto expectedTaskReadDto2;
    private static TaskReadDto expectedTaskStatusReadDto;
    private static TaskReadDto expectedTaskPriorityReadDto;
    private static TaskCreateEditDto taskCreateEditDto1;
    private static TaskCreateEditDto taskCreateEditDto2;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskReadMapper taskReadMapper;

    @Mock
    private TaskCreateEditMapper taskCreateEditMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeAll
    static void setUp() {
        task1 = Task.builder()
                .id(1L)
                .title("Task1")
                .description("Description1")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.LOW)
                .build();

        updatedTask1 = Task.builder()
                .id(1L)
                .title("Task2")
                .description("Description2")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .build();

        updatedStatusTask1 = Task.builder()
                .id(1L)
                .title("Task1")
                .description("Description1")
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.LOW)
                .build();

        updatedPriorityTask1 = Task.builder()
                .id(1L)
                .title("Task1")
                .description("Description1")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.HIGH)
                .build();

        task2 = Task.builder()
                .id(2L)
                .title("Task2")
                .description("Description2")
                .status(TaskStatus.PENDING)
                .priority(TaskPriority.LOW)
                .build();

        expectedTaskReadDto1 = new TaskReadDto(
                1L,
                "Task1",
                "Description1",
                TaskStatus.PENDING,
                TaskPriority.LOW
        );

        expectedTaskReadDto2 = new TaskReadDto(
                1L,
                "Task2",
                "Description2",
                TaskStatus.IN_PROGRESS,
                TaskPriority.HIGH
        );

        expectedTaskStatusReadDto = new TaskReadDto(
                1L,
                "Task1",
                "Description1",
                TaskStatus.IN_PROGRESS,
                TaskPriority.LOW
        );

        expectedTaskPriorityReadDto = new TaskReadDto(
                1L,
                "Task1",
                "Description1",
                TaskStatus.PENDING,
                TaskPriority.HIGH
        );

        taskCreateEditDto1 = new TaskCreateEditDto(
                "Task1",
                "Description1",
                TaskStatus.PENDING,
                TaskPriority.LOW
        );

        taskCreateEditDto2 = new TaskCreateEditDto(
                "Task2",
                "Description2",
                TaskStatus.PENDING,
                TaskPriority.LOW
        );
    }

    @Test
    void findAllTasksShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 2);
        List<Task> expectedTasks = List.of(task1, task2);
        when(taskRepository.findAllByPage(any(Pageable.class)))
                .thenReturn(new PageImpl<>(expectedTasks));

        Page<TaskReadDto> actualTasks = taskService.findAllTasksByPage(pageable);

        assertNotNull(actualTasks);
        assertThat(actualTasks.getTotalElements()).isEqualTo(expectedTasks.size());
        verify(taskRepository).findAllByPage(any(Pageable.class));
    }

    @Test
    void findTaskByIdShouldReturnTask() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task1));
        when(taskReadMapper.map(any(Task.class))).thenReturn(expectedTaskReadDto1);

        TaskReadDto actualTaskReadDto = taskService.findTaskById(TASK_ID);

        assertNotNull(actualTaskReadDto);
        assertThat(actualTaskReadDto).isEqualTo(expectedTaskReadDto1);
        verify(taskRepository).findById(anyLong());
        verify(taskReadMapper).map(any(Task.class));
    }

    @Test
    void findTaskByIdIfNotExistsShouldThrowTaskNotFoundException() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        TaskNotFoundException ex = assertThrows(TaskNotFoundException.class, () -> taskService.findTaskById(TASK_ID));
        assertThat(ex).hasMessage("Task with id " + TASK_ID + " not found");
    }

    @Test
    void createTaskShouldReturnNewTask() {
        when(taskCreateEditMapper.map(taskCreateEditDto1)).thenReturn(task1);
        when(taskRepository.save(any(Task.class))).thenReturn(task1);
        when(taskReadMapper.map(any(Task.class))).thenReturn(expectedTaskReadDto1);

        TaskReadDto actualTaskReadDto = taskService.createTask(taskCreateEditDto1);

        assertNotNull(actualTaskReadDto);
        assertThat(actualTaskReadDto).isEqualTo(expectedTaskReadDto1);
        verify(taskRepository).save(any(Task.class));
        verify(taskCreateEditMapper).map(taskCreateEditDto1);
        verify(taskReadMapper).map(any(Task.class));
    }

    @Test
    void createTaskIfNullDtoShouldThrowTaskCreationException() {
        when(taskCreateEditMapper.map(taskCreateEditDto1)).thenReturn(null);

        TaskCreationException ex = assertThrows(TaskCreationException.class, () -> taskService.createTask(taskCreateEditDto1));
        assertThat(ex).hasMessage("Task creation failed");
    }

    @Test
    void updateTaskShouldReturnUpdatedTask() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task1));
        when(taskCreateEditMapper.map(eq(taskCreateEditDto2), any(Task.class))).thenReturn(updatedTask1);
        when(taskRepository.update(any(Task.class))).thenReturn(Optional.of(updatedTask1));
        when(taskReadMapper.map(any(Task.class))).thenReturn(expectedTaskReadDto2);

        TaskReadDto actualTaskReadDto = taskService.updateTask(TASK_ID, taskCreateEditDto2);

        assertNotNull(actualTaskReadDto);
        assertThat(actualTaskReadDto).isEqualTo(expectedTaskReadDto2);
        assertEquals(expectedTaskReadDto2.getTitle(), actualTaskReadDto.getTitle());
        assertEquals(expectedTaskReadDto2.getDescription(), actualTaskReadDto.getDescription());
        verify(taskRepository).findById(anyLong());
        verify(taskCreateEditMapper).map(eq(taskCreateEditDto2), any(Task.class));
        verify(taskReadMapper).map(any(Task.class));
        verify(taskRepository).update(any(Task.class));
    }

    @Test
    void updateTaskIfNotExistsShouldThrowTaskNotFoundException() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        TaskNotFoundException ex = assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(TASK_ID, taskCreateEditDto2));
        assertThat(ex).hasMessage("Task with id " + TASK_ID + " not found");
    }

    @Test
    void updateTaskIfNullRowsAffectedShouldThrowTaskUpdateException() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task1));
        when(taskCreateEditMapper.map(eq(taskCreateEditDto2), any(Task.class))).thenReturn(updatedTask1);
        when(taskRepository.update(any(Task.class))).thenReturn(Optional.empty());

        TaskUpdateException ex = assertThrows(TaskUpdateException.class, () -> taskService.updateTask(TASK_ID, taskCreateEditDto2));
        assertThat(ex).hasMessage("Task with id " + TASK_ID + " failed to update");
    }

    @Test
    void deleteTaskShouldReturnTrue() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task1));
        when(taskRepository.delete(any(Task.class))).thenReturn(true);

        assertTrue(taskService.deleteTask(TASK_ID));
    }

    @Test
    void deleteTaskIfNotExistsShouldThrowTaskNotFoundException() {
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        TaskNotFoundException ex = assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask(TASK_ID));
        assertThat(ex).hasMessage("Task with id " + TASK_ID + " not found");
    }

    @Test
    void updateTaskStatusShouldReturnUpdatedTask() {
        UpdateStatusDto updateStatusDto = new UpdateStatusDto(TaskStatus.IN_PROGRESS);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task1));
        when(taskRepository.updateTaskStatus(task1)).thenReturn(Optional.of(updatedStatusTask1));
        when(taskReadMapper.map(any(Task.class))).thenReturn(expectedTaskStatusReadDto);

        TaskReadDto actualTaskReadDto = taskService.updateTaskStatus(TASK_ID, updateStatusDto);

        assertNotNull(actualTaskReadDto);
        assertEquals(TaskStatus.IN_PROGRESS, actualTaskReadDto.getStatus());
        assertEquals(TASK_ID, actualTaskReadDto.getId());

        verify(taskRepository).findById(anyLong());
        verify(taskRepository).updateTaskStatus(task1);
        verify(taskReadMapper).map(any(Task.class));
    }

    @Test
    void updateTaskStatusIfNotExistsShouldThrowTaskNotFoundException() {
        UpdateStatusDto updateStatusDto = new UpdateStatusDto(TaskStatus.IN_PROGRESS);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        TaskNotFoundException ex = assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskStatus(TASK_ID, updateStatusDto));
        assertThat(ex).hasMessage("Task with id " + TASK_ID + " not found");
    }

    @Test
    void updateTaskStatusIfNullRowsAffectedShouldThrowTaskUpdateException() {
        UpdateStatusDto updateStatusDto = new UpdateStatusDto(TaskStatus.IN_PROGRESS);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task1));
        when(taskRepository.updateTaskStatus(task1)).thenReturn(Optional.empty());

        TaskUpdateException ex = assertThrows(TaskUpdateException.class, () -> taskService.updateTaskStatus(TASK_ID, updateStatusDto));
        assertThat(ex).hasMessage("Task with id " + TASK_ID + " failed to update");
    }

    @Test
    void updateTaskPriorityShouldReturnUpdatedTask() {
        UpdatePriorityDto updatePriorityDto = new UpdatePriorityDto(TaskPriority.HIGH);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task1));
        when(taskRepository.updateTaskPriority(task1)).thenReturn(Optional.of(updatedPriorityTask1));
        when(taskReadMapper.map(any(Task.class))).thenReturn(expectedTaskPriorityReadDto);

        TaskReadDto actualTaskReadDto = taskService.updateTaskPriority(TASK_ID, updatePriorityDto);

        assertNotNull(actualTaskReadDto);
        assertEquals(TaskPriority.HIGH, actualTaskReadDto.getPriority());
        assertEquals(TASK_ID, actualTaskReadDto.getId());
        verify(taskRepository).findById(anyLong());
        verify(taskRepository).updateTaskPriority(task1);
        verify(taskReadMapper).map(any(Task.class));
    }

    @Test
    void updateTaskPriorityIfNotExistsShouldThrowTaskNotFoundException() {
        UpdatePriorityDto updatePriorityDto = new UpdatePriorityDto(TaskPriority.HIGH);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.empty());

        TaskNotFoundException ex = assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskPriority(TASK_ID, updatePriorityDto));
        assertThat(ex).hasMessage("Task with id " + TASK_ID + " not found");
    }

    @Test
    void updateTaskPriorityIfNullRowsAffectedShouldThrowTaskUpdateException() {
        UpdatePriorityDto updatePriorityDto = new UpdatePriorityDto(TaskPriority.HIGH);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task1));
        when(taskRepository.updateTaskPriority(task1)).thenReturn(Optional.empty());

        TaskUpdateException ex = assertThrows(TaskUpdateException.class, () -> taskService.updateTaskPriority(TASK_ID, updatePriorityDto));
        assertThat(ex).hasMessage("Task with id " + TASK_ID + " failed to update");
    }
}
