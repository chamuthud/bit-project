package com.residential.construction_management.controller;

import com.residential.construction_management.dto.request.TaskRequest;
import com.residential.construction_management.dto.response.TaskResponse;
import com.residential.construction_management.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // Get all tasks, checking if user can access the parent project
    @GetMapping
    @PreAuthorize("@permissionService.canAccessProject(#projectId, authentication)")
    public ResponseEntity<List<TaskResponse>> getAllTasksByProjectId(@PathVariable Long projectId) {
        List<TaskResponse> tasks = taskService.getAllTasksForProject(projectId);
        return ResponseEntity.ok(tasks);
    }

    // Get a specific task, checking if user can access the task (via parent project)
    @GetMapping("/{taskId}")
    @PreAuthorize("@permissionService.canAccessTask(#projectId, #taskId, authentication)")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long projectId, @PathVariable Long taskId) {
        TaskResponse task = taskService.getTaskById(projectId, taskId);
        return ResponseEntity.ok(task);
    }

    // Create a task, checking if user can modify tasks in the parent project
    @PostMapping
    @PreAuthorize("@permissionService.canModifyTasksInProject(#projectId, authentication)")
    public ResponseEntity<TaskResponse> createTask(@PathVariable Long projectId, @Valid @RequestBody TaskRequest taskRequest) {
        TaskResponse createdTask = taskService.createTask(projectId, taskRequest);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    // Update a task, checking if user can modify tasks in the parent project
    @PutMapping("/{taskId}")
    @PreAuthorize("@permissionService.canModifyTasksInProject(#projectId, authentication)")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long projectId, @PathVariable Long taskId, @Valid @RequestBody TaskRequest taskRequest) {
        // Optional: Add finer check if needed (e.g., can only update tasks assigned to self)
        TaskResponse updatedTask = taskService.updateTask(projectId, taskId, taskRequest);
        return ResponseEntity.ok(updatedTask);
    }

    // Delete a task, checking if user can modify tasks in the parent project
    @DeleteMapping("/{taskId}")
    @PreAuthorize("@permissionService.canModifyTasksInProject(#projectId, authentication)")
    public ResponseEntity<?> deleteTask(@PathVariable Long projectId, @PathVariable Long taskId) {
        taskService.deleteTask(projectId, taskId);
        return ResponseEntity.ok().build();
    }
}
