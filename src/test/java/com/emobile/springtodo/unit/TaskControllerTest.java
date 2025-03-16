package com.emobile.springtodo.unit;

import com.emobile.springtodo.dto.TaskCreateEditDto;
import com.emobile.springtodo.dto.TaskReadDto;
import com.emobile.springtodo.dto.UpdatePriorityDto;
import com.emobile.springtodo.dto.UpdateStatusDto;
import com.emobile.springtodo.exception.TaskCreationException;
import com.emobile.springtodo.exception.TaskNotFoundException;
import com.emobile.springtodo.exception.TaskUpdateException;
import com.emobile.springtodo.http.controller.TaskController;
import com.emobile.springtodo.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.emobile.springtodo.model.enums.TaskPriority.*;
import static com.emobile.springtodo.model.enums.TaskStatus.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @MockBean
    private TaskService taskService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void findTasksByPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 5);
        TaskReadDto task1 = createTaskDto(1L);
        TaskReadDto task2 = createTaskDto(2L);
        Page<TaskReadDto> page = new PageImpl<>(List.of(task1, task2), pageable, 2);

        when(taskService.findAllTasksByPage(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void findTaskByIdShouldReturnTask() throws Exception {
        TaskReadDto task = createTaskDto(1L);

        when(taskService.findTaskById(anyLong())).thenReturn(task);

        mockMvc.perform(get("/api/v1/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.status").value(PENDING.toString()))
                .andExpect(jsonPath("$.priority").value(LOW.toString()));
    }

    @Test
    void findTaskByIdShouldReturnNotFound() throws Exception {
        when(taskService.findTaskById(100L)).thenThrow(new TaskNotFoundException("Task with id " + 100L + " not found"));

        mockMvc.perform(get("/api/v1/tasks/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTaskShouldReturnCreatedTask() throws Exception {
        TaskCreateEditDto createRequest = new TaskCreateEditDto(
                "Task 1",
                "Description 1",
                PENDING,
                LOW
        );
        TaskReadDto response = createTaskDto(1L);
        when(taskService.createTask(any(TaskCreateEditDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createTaskShouldReturnBadRequest() throws Exception {
        TaskCreateEditDto createRequest = new TaskCreateEditDto(
                "Task 1",
                "Description 1",
                PENDING,
                LOW
        );
        when(taskService.createTask(any(TaskCreateEditDto.class))).thenThrow(new TaskCreationException("Task creation failed"));

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTaskWithInvalidRequestShouldReturnBadRequest() throws Exception {
        TaskCreateEditDto createRequest = new TaskCreateEditDto(" ", " ", PENDING, LOW);

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Title can not be empty"));;
    }

    @Test
    void updateTaskShouldReturnUpdatedTask() throws Exception {
        TaskCreateEditDto updateRequest = new TaskCreateEditDto(
                "Task 1",
                "Description 1",
                PENDING,
                LOW
        );
        TaskReadDto response = createTaskDto(1L);
        when(taskService.updateTask(anyLong(), any(TaskCreateEditDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description 1"))
                .andExpect(jsonPath("$.status").value(PENDING.toString()))
                .andExpect(jsonPath("$.priority").value(LOW.toString()));
    }

    @Test
    void updateTaskShouldReturnBadRequest() throws Exception {
        TaskCreateEditDto updateRequest = new TaskCreateEditDto(
                "Task 1",
                "Description 1",
                PENDING,
                LOW
        );
        when(taskService.updateTask(anyLong(), any(TaskCreateEditDto.class))).thenThrow(new TaskUpdateException("Task with id " + 100L + " failed to update"));

        mockMvc.perform(put("/api/v1/tasks/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteTaskShouldReturnNoContent() throws Exception {
        when(taskService.deleteTask(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/tasks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTaskIfNotExistsShouldReturnNotFound() throws Exception {
        when(taskService.deleteTask(100L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/tasks/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTaskStatusShouldReturnUpdatedTask() throws Exception {
        UpdateStatusDto updateRequest = new UpdateStatusDto(PENDING);
        TaskReadDto response = createTaskDto(1L);
        when(taskService.updateTaskStatus(anyLong(), any(UpdateStatusDto.class))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/tasks/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value(PENDING.toString()));
    }

    @Test
    void updateTaskPriorityShouldReturnUpdatedTask() throws Exception {
        UpdatePriorityDto updateRequest = new UpdatePriorityDto(LOW);
        TaskReadDto response = createTaskDto(1L);
        when(taskService.updateTaskPriority(anyLong(), any(UpdatePriorityDto.class))).thenReturn(response);

        mockMvc.perform(patch("/api/v1/tasks/1/priority")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.priority").value(LOW.toString()));
    }

    private TaskReadDto createTaskDto(Long id) {
        return new TaskReadDto(
                id,
                "Task 1",
                "Description 1",
                PENDING,
                LOW
        );
    }
}
