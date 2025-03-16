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

/**
 * Контроллер для управления задачами.
 *
 * @see TaskReadDto
 * @see TaskCreateEditDto
 * @see UpdateStatusDto
 * @see UpdatePriorityDto
 */
@Tag(
        name = "Task Controller",
        description = "Контроллер для управления задачами"
)
public interface TaskController {

    /**
     * Найти задачи.
     * <p>
     * Этот метод позволяет найти задачи с учетом пагинации. Пагинация передается с помощью параметра {@link Pageable}.
     * </p>
     *
     * @param pageable Параметры пагинации.
     * @return {@link ResponseEntity} с найденными задачами в виде страницы.
     * @see Pageable
     */
    @Operation(
            summary = "Найти задачи",
            description = "Находит все задачи по заданной странице (Pageable)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Задачи успешно найдены")
            }
    )
    ResponseEntity<Page<TaskReadDto>> findTasks(Pageable pageable);

    /**
     * Получить задачу по идентификатору.
     * <p>
     * Этот метод возвращает задачу по указанному идентификатору.
     * </p>
     *
     * @param id Идентификатор задачи.
     * @return {@link ResponseEntity} с найденной задачей.
     */
    @Operation(
            summary = "Получить задачу по идентификатору",
            description = "Возвращает задачу с указанным идентификатором",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Задача успешно найдена"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            }
    )
    ResponseEntity<TaskReadDto> findTaskById(Long id);

    /**
     * Создать новую задачу.
     * <p>
     * Этот метод создает новую задачу на основе переданных данных.
     * </p>
     *
     * @param task Данные для создания задачи.
     * @return {@link ResponseEntity} с созданной задачей.
     */
    @Operation(
            summary = "Создать новую задачу",
            description = "Создаёт новую задачу на основе переданных данных",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
                    @ApiResponse(responseCode = "400", description = "Некорректные данные")
            }
    )
    ResponseEntity<TaskReadDto> createTask(TaskCreateEditDto task);

    /**
     * Обновить задачу.
     * <p>
     * Этот метод позволяет обновить существующую задачу на основе переданных данных и идентификатора задачи.
     * </p>
     *
     * @param id   Идентификатор задачи для обновления.
     * @param task Данные для обновления задачи.
     * @return {@link ResponseEntity} с обновленной задачей.
     */
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

    /**
     * Удалить задачу.
     * <p>
     * Этот метод удаляет задачу по заданному идентификатору.
     * </p>
     *
     * @param id Идентификатор задачи для удаления.
     * @return {@link ResponseEntity} без содержимого с кодом ответа 204.
     */
    @Operation(
            summary = "Удалить задачу",
            description = "Удаляет задачу по заданному идентификатору",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Задача успешно удалена"),
                    @ApiResponse(responseCode = "404", description = "Задача не найдена")
            }
    )
    ResponseEntity<Void> deleteTask(Long id);

    /**
     * Обновить статус задачи.
     * <p>
     * Этот метод позволяет обновить статус задачи по заданному идентификатору. В случае успешного обновления возвращается обновленная задача.
     * </p>
     *
     * @param id     Идентификатор задачи для обновления.
     * @param status Новый статус задачи.
     * @return {@link ResponseEntity} с обновленной задачей.
     */
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

    /**
     * Обновить приоритет задачи.
     * <p>
     * Этот метод позволяет обновить приоритет задачи по заданному идентификатору. В случае успешного обновления возвращается обновленная задача.
     * </p>
     *
     * @param id       Идентификатор задачи для обновления.
     * @param priority Новый приоритет задачи.
     * @return {@link ResponseEntity} с обновленной задачей.
     */
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
