package com.residential.construction_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskRequest {

    @NotBlank(message = "Task name cannot be blank")
    @Size(max = 150)
    private String taskName;

    private String description;

    private Integer priority;

    @Size(max = 30)
    private String taskStatus; // e.g., "To Do", "In Progress", "Done"

    private LocalDate startDate;

    private LocalDate endDate;

    // Project ID will be taken from the path variable in the controller
    // Assigned-to User ID might be optional or set separately
    private Long assignedToUserId;
}
