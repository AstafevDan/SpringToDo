package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность для обновления статуса задачи")
public class UpdateStatusDto {

    @NotNull(message = "Status can not be empty (Only PENDING, IN_PROGRESS, COMPLETED value)")
    @Schema(description = "Статус задачи", allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED"})
    private TaskStatus status;
}
