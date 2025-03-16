package com.emobile.springtodo.exception;

/**
 * Исключение, выбрасываемое при ошибке обновления задачи.
 */
public class TaskUpdateException extends RuntimeException {

    /**
     * Создаёт исключение с указанным сообщением об ошибке.
     *
     * @param message Сообщение об ошибке.
     */
    public TaskUpdateException(String message) {
        super(message);
    }
}
