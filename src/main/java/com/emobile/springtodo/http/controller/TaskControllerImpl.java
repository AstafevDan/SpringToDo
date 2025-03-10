package com.emobile.springtodo.http.controller;

import com.emobile.springtodo.dto.TaskCreateEditDto;
import com.emobile.springtodo.dto.TaskReadDto;
import com.emobile.springtodo.dto.UpdatePriorityDto;
import com.emobile.springtodo.dto.UpdateStatusDto;
import com.emobile.springtodo.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TaskControllerImpl implements TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<Page<TaskReadDto>> findTasks(@PageableDefault(
            size = 5,
            sort = {"priority"}
    ) Pageable pageable) {
        Page<TaskReadDto> allTasksByPage = taskService.findAllTasksByPage(pageable);
        return ResponseEntity.ok(allTasksByPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskReadDto> findTaskById(@PathVariable Long id) {
        TaskReadDto task = taskService.findTaskById(id);
        return ResponseEntity.ok(task);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TaskReadDto> createTask(@RequestBody TaskCreateEditDto task) {
        TaskReadDto createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskReadDto> updateTask(@PathVariable Long id, @RequestBody TaskCreateEditDto task) {
        TaskReadDto updatedTask = taskService.updateTask(id, task);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        return taskService.deleteTask(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskReadDto> updateTaskStatus(@PathVariable Long id, @RequestBody UpdateStatusDto status) {
        TaskReadDto updatedTask = taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{id}/priority")
    public ResponseEntity<TaskReadDto> updateTaskPriority(@PathVariable Long id, @RequestBody UpdatePriorityDto priority) {
        TaskReadDto updatedTask = taskService.updateTaskPriority(id, priority);
        return ResponseEntity.ok(updatedTask);
    }
}
