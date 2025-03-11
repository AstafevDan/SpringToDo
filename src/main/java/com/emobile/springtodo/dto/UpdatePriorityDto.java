package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.enums.TaskPriority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сущность для обновления приоритета задачи")
public class UpdatePriorityDto {

    @NotNull(message = "Priority can not be empty (Only LOW, MEDIUM, HIGH value)")
    @Schema(description = "Приоритет задачи")
    private TaskPriority priority;
}
