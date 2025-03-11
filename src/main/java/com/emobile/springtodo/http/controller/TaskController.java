package com.emobile.springtodo.http.controller;

import com.emobile.springtodo.dto.TaskCreateEditDto;
import com.emobile.springtodo.dto.TaskReadDto;
import com.emobile.springtodo.dto.UpdatePriorityDto;
import com.emobile.springtodo.dto.UpdateStatusDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

@Tag(
        name = "Task Controller",
        description = "Контроллер для управления задачами"
)
public interface TaskController {

    @Operation(
            summary = "Найти задачи",
            description = "Находит все задачи по заданной странице (Pageable)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Задачи успешно найдены")
            }
    )
    ResponseEntity<Page<TaskReadDto>> findTasks(Pageable pageable);

    @Operation(
            summary = "Получить задачу по идентификатору",
            description = "Возвращает задачу с указанным идентификатором",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно найдена"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            }
    )
    ResponseEntity<TaskReadDto> findTaskById(Long id);

    @Operation(
            summary = "Создать новую задачу",
            description = "Создаёт новую задачу на основе переданных данных",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    ResponseEntity<TaskReadDto> createTask(TaskCreateEditDto task);

    @Operation(
            summary = "Обновить задачу",
            description = "Обновляет существующую задачу по заданному идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно обновлена"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            }
    )
    ResponseEntity<TaskReadDto> updateTask(Long id, TaskCreateEditDto task);

    @Operation(
            summary = "Удалить задачу",
            description = "Удаляет задачу по заданному идентификатору",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Задача успешно удалена"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            }
    )
    ResponseEntity<Void> deleteTask(Long id);

    @Operation(
            summary = "Обновить статус задачи",
            description = "Обновляет статус существующей задачи по заданному идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Статус задачи успешно обновлен"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            }
    )
    ResponseEntity<TaskReadDto> updateTaskStatus(Long id, UpdateStatusDto status);

    @Operation(
            summary = "Обновить приоритет задачи",
            description = "Обновляет приоритет существующей задачи по заданному идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Приоритет задачи успешно обновлен"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            }
    )
    ResponseEntity<TaskReadDto> updateTaskPriority(Long id, UpdatePriorityDto priority);
}
