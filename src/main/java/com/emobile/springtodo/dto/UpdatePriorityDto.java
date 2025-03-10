package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.enums.TaskPriority;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePriorityDto {
    private TaskPriority priority;
}
