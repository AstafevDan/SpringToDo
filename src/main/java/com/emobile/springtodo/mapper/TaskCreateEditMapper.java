package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.TaskCreateEditDto;
import com.emobile.springtodo.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskCreateEditMapper implements Mapper<TaskCreateEditDto, Task> {

    @Override
    public Task map(TaskCreateEditDto object) {
        return Task.builder()
                .title(object.getTitle())
                .description(object.getDescription())
                .status(object.getStatus())
                .priority(object.getPriority())
                .build();
    }

    @Override
    public Task map(TaskCreateEditDto fromObject, Task toObject) {
        if (fromObject.getTitle() != null) {
            toObject.setTitle(fromObject.getTitle());
        }
        if (fromObject.getDescription() != null) {
            toObject.setDescription(fromObject.getDescription());
        }
        if (fromObject.getStatus() != null) {
            toObject.setStatus(fromObject.getStatus());
        }
        if (fromObject.getPriority() != null) {
            toObject.setPriority(fromObject.getPriority());
        }

        return toObject;
    }
}
