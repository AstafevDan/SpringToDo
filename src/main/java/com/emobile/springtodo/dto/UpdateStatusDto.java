package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO для обновления статуса задачи.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность для обновления статуса задачи")
public class UpdateStatusDto {

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
}
