package com.residential.construction_management.dto.response;

import com.residential.construction_management.model.Project;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ProjectResponse {

    private Long projectId;
    private String projectName;
    private String description;
    private String projectType;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long createdByUserId; // ID of the user who created it
    private String createdByUsername; // Username of the user
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Static factory method for conversion
    public static ProjectResponse fromEntity(Project project) {
        ProjectResponse dto = new ProjectResponse();
        dto.setProjectId(project.getProjectId());
        dto.setProjectName(project.getProjectName());
        dto.setDescription(project.getDescription());
        dto.setProjectType(project.getProjectType());
        dto.setStatus(project.getStatus());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        if (project.getCreatedBy() != null) {
            dto.setCreatedByUserId(project.getCreatedBy().getUserId());
            dto.setCreatedByUsername(project.getCreatedBy().getUsername()); // Assuming User entity has getUsername()
        }
        dto.setCreatedAt(project.getCreatedAt());
        dto.setUpdatedAt(project.getUpdatedAt());
        return dto;
    }
}