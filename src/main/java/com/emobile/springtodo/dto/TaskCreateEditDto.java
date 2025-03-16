package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.enums.TaskPriority;
import com.emobile.springtodo.model.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO для создания и обновления задачи.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность для создания задачи")
public class TaskCreateEditDto {

    /**
     * Название задачи.
     * <p>
     * Данное поле не может быть пустым и должно содержать от 1 до 64 символов.
     * </p>
     */
    @Size(min = 1, max = 64)
    @NotBlank(message = "Title can not be empty")
    @Schema(description = "Название задачи", example = "Title")
    private String title;

    /**
     * Описание задачи.
     * <p>
     * Это необязательное поле, которое может содержать текст, описывающий задачу.
     * </p>
     */
    @Schema(description = "Описание задачи", example = "Something about task")
    private String description;

    /**
     * Статус задачи.
     * <p>
     * Это обязательное поле, которое указывает на статус задачи. Статус может быть одним из следующих:
     * PENDING, IN_PROGRESS, COMPLETED.
     * </p>
     *
     * @see TaskStatus
     */
    @NotNull(message = "Status can not be empty (Only PENDING, IN_PROGRESS, COMPLETED value)")
    @Schema(description = "Статус задачи")
    private TaskStatus status;

    /**
     * Приоритет задачи.
     * <p>
     * Это обязательное поле, которое указывает на приоритет задачи. Приоритет может быть одним из следующих:
     * LOW, MEDIUM, HIGH.
     * </p>
     *
     * @see TaskPriority
     */
    @NotNull(message = "Priority can not be empty (Only LOW, MEDIUM, HIGH value)")
    @Schema(description = "Приоритет задачи")
    private TaskPriority priority;
}
