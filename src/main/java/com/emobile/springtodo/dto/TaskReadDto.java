package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.enums.TaskPriority;
import com.emobile.springtodo.model.enums.TaskStatus;
import lombok.Value;

@Value
public class TaskReadDto {

    Long id;

    String title;

    String description;

    TaskStatus status;

    TaskPriority priority;
}
