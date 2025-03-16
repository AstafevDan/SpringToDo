package com.emobile.springtodo.exception;

/**
 * Исключение, выбрасываемое при отсутствии задачи с указанным идентификатором.
 */
public class TaskNotFoundException extends RuntimeException {

    /**
     * Создаёт исключение с указанным сообщением об ошибке.
     *
     * @param message Сообщение об ошибке.
     */
    public TaskNotFoundException(String message) {
        super(message);
    }
}
