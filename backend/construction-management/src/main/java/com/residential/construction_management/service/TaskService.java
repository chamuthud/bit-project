package com.residential.construction_management.service;

import com.residential.construction_management.dto.request.TaskRequest;
import com.residential.construction_management.dto.response.TaskResponse;
import com.residential.construction_management.exception.ResourceNotFoundException;

import com.residential.construction_management.model.Project;
import com.residential.construction_management.model.Task;
import com.residential.construction_management.model.User;
import com.residential.construction_management.repository.ProjectRepository;
import com.residential.construction_management.repository.TaskRepository;
import com.residential.construction_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;


@Service
public class TaskService {


    @Autowired
    private TaskRepository taskRepository;


    @Autowired
    private ProjectRepository projectRepository;


    @Autowired
    private UserRepository userRepository;





    @Transactional(readOnly = true)
    public List<TaskResponse> getAllTasksForProject(Long projectId) {
        // Verify project exists first
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project", "id", projectId);
        }
        return taskRepository.findByProjectProjectId(projectId).stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long projectId, Long taskId) {
        Task task = taskRepository.findByTaskIdAndProjectProjectId(taskId, projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId + " within project " + projectId));
        return TaskResponse.fromEntity(task);
    }


    @Transactional
    public TaskResponse createTask(Long projectId, TaskRequest taskRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));


        User assignedUser = null;
        if (taskRequest.getAssignedToUserId() != null) {
            assignedUser = userRepository.findById(taskRequest.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", taskRequest.getAssignedToUserId()));
        }


        Task task = new Task();
        task.setTaskName(taskRequest.getTaskName());
        task.setDescription(taskRequest.getDescription());
        task.setPriority(taskRequest.getPriority());
        task.setTaskStatus(taskRequest.getTaskStatus()); // Consider a default status like "To Do"
        task.setStartDate(taskRequest.getStartDate());
        task.setEndDate(taskRequest.getEndDate());
        task.setProject(project); // Link to the project
        task.setAssignedTo(assignedUser); // Link to assigned user (can be null)


        Task savedTask = taskRepository.save(task);
        return TaskResponse.fromEntity(savedTask);
    }


    @Transactional
    public TaskResponse updateTask(Long projectId, Long taskId, TaskRequest taskRequest) {
        // Verify project exists first
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project", "id", projectId);
        }


        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));


        // Basic check: Ensure the task belongs to the specified project
        if (!task.getProject().getProjectId().equals(projectId)) {
            throw new IllegalArgumentException("Task with ID " + taskId + " does not belong to project with ID " + projectId);
        }


        User assignedUser = null;
        if (taskRequest.getAssignedToUserId() != null) {
            assignedUser = userRepository.findById(taskRequest.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", taskRequest.getAssignedToUserId()));
        }


        task.setTaskName(taskRequest.getTaskName());
        task.setDescription(taskRequest.getDescription());
        task.setPriority(taskRequest.getPriority());
        task.setTaskStatus(taskRequest.getTaskStatus());
        task.setStartDate(taskRequest.getStartDate());
        task.setEndDate(taskRequest.getEndDate());
        task.setAssignedTo(assignedUser);


        Task updatedTask = taskRepository.save(task);
        return TaskResponse.fromEntity(updatedTask);
    }


    @Transactional
    public void deleteTask(Long projectId, Long taskId) {
        // Verify project exists first
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project", "id", projectId);
        }


        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));


        // Basic check: Ensure the task belongs to the specified project
        if (!task.getProject().getProjectId().equals(projectId)) {
            throw new IllegalArgumentException("Task with ID " + taskId + " does not belong to project with ID " + projectId);
        }


        taskRepository.delete(task);
    }
}
