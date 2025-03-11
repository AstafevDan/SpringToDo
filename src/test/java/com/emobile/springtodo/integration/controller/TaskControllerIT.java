package com.emobile.springtodo.integration.controller;

import com.emobile.springtodo.dto.TaskCreateEditDto;
import com.emobile.springtodo.dto.UpdatePriorityDto;
import com.emobile.springtodo.dto.UpdateStatusDto;
import com.emobile.springtodo.integration.IntegrationTestBase;
import com.emobile.springtodo.model.enums.TaskPriority;
import com.emobile.springtodo.model.enums.TaskStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class TaskControllerIT extends IntegrationTestBase {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Test
    void findAllTasksByPage() throws Exception {
        mockMvc.perform(get("/api/v1/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[2].id").value(3))
                .andExpect(jsonPath("$.content[3].id").value(4))
                .andExpect(jsonPath("$.content[4].id").value(5))
                .andExpect(jsonPath("$.totalElements").value(5));
    }

    @Test
    void findTaskById() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.title").value("task5"))
                .andExpect(jsonPath("$.description").value("description5"))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));
    }

    @Test
    void createTask() throws Exception {
        TaskCreateEditDto taskCreateEditDto = new TaskCreateEditDto(
                "new task",
                "new description",
                TaskStatus.PENDING,
                TaskPriority.MEDIUM
        );

        mockMvc.perform(post("/api/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreateEditDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(6))
                .andExpect(jsonPath("$.title").value("new task"))
                .andExpect(jsonPath("$.description").value("new description"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));
    }

    @Test
    void updateTask() throws Exception {
        TaskCreateEditDto taskCreateEditDto = new TaskCreateEditDto(
                "updated title",
                null,
                TaskStatus.COMPLETED,
                TaskPriority.MEDIUM
        );

        mockMvc.perform(put("/api/v1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreateEditDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("updated title"))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"));
    }

    @Test
    void deleteTask() throws Exception {
        mockMvc.perform(delete("/api/v1/tasks/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateTaskStatus() throws Exception {
        UpdateStatusDto updateStatusDto = new UpdateStatusDto(TaskStatus.COMPLETED);

        mockMvc.perform(patch("/api/v1/tasks/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateStatusDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void updateTaskPriority() throws Exception {
        UpdatePriorityDto updatePriorityDto = new UpdatePriorityDto(TaskPriority.HIGH);

        mockMvc.perform(patch("/api/v1/tasks/1/priority")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatePriorityDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.priority").value("HIGH"));
    }
}
