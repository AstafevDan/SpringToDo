package com.emobile.springtodo.exception;

/**
 * Исключение, выбрасываемое при ошибке создания новой задачи.
 */
public class TaskCreationException extends RuntimeException {

    /**
     * Создаёт исключение с указанным сообщением об ошибке.
     *
     * @param message Сообщение об ошибке.
     */
    public TaskCreationException(String message) {
        super(message);
    }
}
