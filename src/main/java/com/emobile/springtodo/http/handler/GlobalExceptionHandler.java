package com.emobile.springtodo.http.handler;

import com.emobile.springtodo.exception.TaskCreationException;
import com.emobile.springtodo.exception.TaskNotFoundException;
import com.emobile.springtodo.exception.TaskUpdateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Обработчик глобальных исключений для приложения.
 * <p>В зависимости от типа исключения, возвращается различный HTTP статус и сообщение, информирующее пользователя о возникшей проблеме.</p>
 */
@RestControllerAdvice(basePackages = "com.emobile.springtodo.http.controller")
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключение {@link MethodArgumentNotValidException}, которое возникает при неправильной валидации данных запроса.
     * <p>
     * Этот метод перехватывает ошибку валидации, собирает все ошибки валидации полей и возвращает их в ответе с кодом статуса 400.
     * </p>
     *
     * @param ex Исключение, которое содержит информацию о несоответствии данных.
     * @return {@link ResponseEntity} с {@link HashMap} ошибок, где ключами являются поля, а значениями - сообщения об ошибках.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключение {@link TaskNotFoundException}, которое возникает, когда задача не найдена.
     * <p>
     * Этот метод возвращает сообщение об ошибке с кодом 404 в случае, если задача с указанным идентификатором не была найдена.
     * </p>
     *
     * @param ex Исключение, которое содержит сообщение о ненайденной задаче.
     * @return {@link ResponseEntity} с сообщением об ошибке и статусом 404.
     */
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    /**
     * Обрабатывает исключение {@link TaskCreationException}, которое возникает при ошибке создания задачи.
     * <p>
     * Этот метод возвращает сообщение об ошибке с кодом 400, если при попытке создать задачу возникли ошибки.
     * </p>
     *
     * @param ex Исключение, которое содержит сообщение о проблеме при создании задачи.
     * @return {@link ResponseEntity} с сообщением об ошибке и статусом 400.
     */
    @ExceptionHandler(TaskCreationException.class)
    public ResponseEntity<String> handleTaskCreationException(TaskCreationException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает исключение {@link TaskUpdateException}, которое возникает при ошибке обновления задачи.
     * <p>
     * Этот метод возвращает сообщение об ошибке с кодом 400, если при обновлении задачи возникли проблемы.
     * </p>
     *
     * @param ex Исключение, которое содержит сообщение о проблемах при обновлении задачи.
     * @return {@link ResponseEntity} с сообщением об ошибке и статусом 400.
     */
    @ExceptionHandler(TaskUpdateException.class)
    public ResponseEntity<String> handleTaskUpdateException(TaskUpdateException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    /**
     * Обрабатывает все остальные непредвиденные исключения.
     * <p>
     * Этот метод перехватывает любые необработанные исключения, которые могут возникнуть в приложении, и возвращает
     * сообщение об ошибке с кодом 500.
     * </p>
     *
     * @param ex Исключение, которое было перехвачено и не соответствует другим обработчикам.
     * @return {@link ResponseEntity} с сообщением об ошибке и статусом 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return new ResponseEntity<>("An error has occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
