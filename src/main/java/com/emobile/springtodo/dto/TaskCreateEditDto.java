package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.enums.TaskPriority;
import com.emobile.springtodo.model.enums.TaskStatus;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateEditDto {

    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;
}
