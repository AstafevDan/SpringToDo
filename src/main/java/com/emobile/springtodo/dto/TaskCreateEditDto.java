package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.enums.TaskPriority;
import com.emobile.springtodo.model.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность для создания задачи")
public class TaskCreateEditDto {

    @Size(min = 1, max = 64)
    @NotBlank(message = "Title can not be empty")
    @Schema(description = "Название задачи", example = "Title")
    private String title;

    @Schema(description = "Описание задачи", example = "Something about task")
    private String description;

    @NotNull(message = "Status can not be empty (Only PENDING, IN_PROGRESS, COMPLETED value)")
    @Schema(description = "Статус задачи", allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED"})
    private TaskStatus status;

    @NotNull(message = "Priority can not be empty (Only LOW, MEDIUM, HIGH value)")
    @Schema(description = "Приоритет задачи", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    private TaskPriority priority;
}
