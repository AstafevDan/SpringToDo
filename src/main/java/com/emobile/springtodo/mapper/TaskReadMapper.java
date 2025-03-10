package com.emobile.springtodo.mapper;

import com.emobile.springtodo.dto.TaskReadDto;
import com.emobile.springtodo.model.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskReadMapper implements Mapper<Task, TaskReadDto> {

    @Override
    public TaskReadDto map(Task object) {
        return new TaskReadDto(
                object.getId(),
                object.getTitle(),
                object.getDescription(),
                object.getStatus(),
                object.getPriority()
        );
    }
}
