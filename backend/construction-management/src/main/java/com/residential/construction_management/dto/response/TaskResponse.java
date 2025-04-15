package com.residential.construction_management.dto.response;

import com.residential.construction_management.model.Task;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class TaskResponse {
    private Long taskId;
    private String taskName;
    private String description;
    private Integer priority;
    private String taskStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long projectId; // ID of the parent project
    private Long assignedToUserId; // ID of the assigned user (if any)
    private String assignedToUsername; // Username of the assigned user (if any)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // Static factory method
    public static TaskResponse fromEntity(Task task) {
        TaskResponse dto = new TaskResponse();
        dto.setTaskId(task.getTaskId());
        dto.setTaskName(task.getTaskName());
        dto.setDescription(task.getDescription());
        dto.setPriority(task.getPriority());
        dto.setTaskStatus(task.getTaskStatus());
        dto.setStartDate(task.getStartDate());
        dto.setEndDate(task.getEndDate());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());

        if (task.getProject() != null) {
            dto.setProjectId(task.getProject().getProjectId());
        }
        if (task.getAssignedTo() != null) {
            dto.setAssignedToUserId(task.getAssignedTo().getUserId());
            dto.setAssignedToUsername(task.getAssignedTo().getUsername()); // Assuming User has getUsername()
        }
        return dto;
    }
}
