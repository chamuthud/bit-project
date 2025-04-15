package com.residential.construction_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProjectRequest {

    @NotBlank(message = "Project name cannot be blank")
    @Size(max = 100, message = "Project name cannot exceed 100 characters")
    private String projectName;

    private String description;

    @Size(max = 50)
    private String projectType;

    @Size(max = 30)
    private String status;

    private LocalDate startDate;

    private LocalDate endDate;

    // createdBy will be set from the authenticated user in the service layer
}
