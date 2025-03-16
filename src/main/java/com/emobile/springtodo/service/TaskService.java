package com.emobile.springtodo.service;

import com.emobile.springtodo.dto.TaskCreateEditDto;
import com.emobile.springtodo.dto.TaskReadDto;
import com.emobile.springtodo.dto.UpdatePriorityDto;
import com.emobile.springtodo.dto.UpdateStatusDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Сервис для управления задачами.
 */
public interface TaskService {

    /**
     * Возвращает страницу задач с учётом параметров пагинации.
     *
     * @param pageable Параметры пагинации.
     * @return Страница задач в виде DTO {@link TaskReadDto}.
     */
    Page<TaskReadDto> findAllTasksByPage(Pageable pageable);

    /**
     * Находит задачу по её идентификатору.
     *
     * @param id Идентификатор задачи.
     * @return DTO найденной задачи {@link TaskReadDto}.
     */
    TaskReadDto findTaskById(Long id);

    /**
     * Создаёт новую задачу на основе переданных данных.
     *
     * @param taskCreateEditDto Данные в виде DTO для создания задачи.
     * @return DTO созданной задачи {@link TaskReadDto}.
     */
    TaskReadDto createTask(TaskCreateEditDto taskCreateEditDto);

    /**
     * Обновляет существующую задачу по её идентификатору.
     *
     * @param id                Идентификатор задачи для обновления.
     * @param taskCreateEditDto Данные в виде DTO для обновления задачи.
     * @return DTO обновлённой задачи {@link TaskReadDto}.
     */
    TaskReadDto updateTask(Long id, TaskCreateEditDto taskCreateEditDto);

    /**
     * Удаляет задачу по её идентификатору.
     *
     * @param id Идентификатор задачи для удаления.
     * @return {@code true}, если задача была успешно удалена, иначе {@code false}.
     */
    boolean deleteTask(Long id);

    /**
     * Обновляет статус задачи по её идентификатору.
     *
     * @param id     Идентификатор задачи.
     * @param status DTO с новым статусом задачи.
     * @return DTO обновлённой задачи {@link TaskReadDto}.
     */
    TaskReadDto updateTaskStatus(Long id, UpdateStatusDto status);

    /**
     * Обновляет приоритет задачи по её идентификатору.
     *
     * @param id       Идентификатор задачи.
     * @param priority DTO с новым приоритетом задачи.
     * @return DTO обновлённой задачи {@link TaskReadDto}.
     */
    TaskReadDto updateTaskPriority(Long id, UpdatePriorityDto priority);
}
