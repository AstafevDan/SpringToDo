package com.emobile.springtodo.repository;

import com.emobile.springtodo.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Репозиторий для управления сущностями {@link Task}.
 */
public interface TaskRepository {

    /**
     * Находит все задачи с поддержкой постраничного вывода.
     *
     * @param pageable Параметры пагинации.
     * @return Страница задач.
     */
    Page<Task> findAllByPage(Pageable pageable);

    /**
     * Ищет задачу по её идентификатору.
     *
     * @param id Идентификатор задачи.
     * @return Найденная задача, если она существует.
     */
    Optional<Task> findById(Long id);

    /**
     * Сохраняет новую задачу.
     *
     * @param task Сохраняемая задача.
     * @return Сохранённая задача.
     */
    Task save(Task task);

    /**
     * Обновляет существующую задачу.
     *
     * @param task Обновляемая задача.
     * @return Обновлённая задача, если обновление прошло успешно.
     */
    Optional<Task> update(Task task);

    /**
     * Удаляет задачу.
     *
     * @param task Удаляемая задача.
     * @return {@code true}, если задача была успешно удалена, иначе {@code false}.
     */
    boolean delete(Task task);

    /**
     * Обновляет приоритет задачи.
     *
     * @param task Задача с новым приоритетом.
     * @return Обновлённая задача с новым приоритетом, если обновление прошло успешно.
     */
    Optional<Task> updateTaskPriority(Task task);

    /**
     * Обновляет статус задачи.
     *
     * @param task Задача с новым статусом.
     * @return Обновлённая задача с новым статусом, если обновление прошло успешно.
     */
    Optional<Task> updateTaskStatus(Task task);
}
