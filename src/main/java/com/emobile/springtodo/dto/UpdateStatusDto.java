package com.emobile.springtodo.dto;

import com.emobile.springtodo.model.enums.TaskStatus;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusDto {
    private TaskStatus status;
}
