package com.emobile.springtodo.dto;

import com.emobile.springtodo.config.serializer.EnumSerializer;
import com.emobile.springtodo.config.deserializer.TaskPriorityDeserializer;
import com.emobile.springtodo.config.deserializer.TaskStatusDeserializer;
import com.emobile.springtodo.model.enums.TaskPriority;
import com.emobile.springtodo.model.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
@NoArgsConstructor(force = true)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
@Schema(description = "Сущность задачи")
public class TaskReadDto implements Serializable {

    @Schema(description = "Уникальный идентификатор задачи")
    Long id;

    @Schema(description = "Название задачи", example = "Title")
    String title;

    @Schema(description = "Описание задачи", example = "Something about task")
    String description;

    @JsonSerialize(using = EnumSerializer.class)
    @JsonDeserialize(using = TaskStatusDeserializer.class)
    @Schema(description = "Статус задачи", allowableValues = {"PENDING", "IN_PROGRESS", "COMPLETED"})
    TaskStatus status;

    @JsonSerialize(using = EnumSerializer.class)
    @JsonDeserialize(using = TaskPriorityDeserializer.class)
    @Schema(description = "Приоритет задачи", allowableValues = {"LOW", "MEDIUM", "HIGH"})
    TaskPriority priority;
}
