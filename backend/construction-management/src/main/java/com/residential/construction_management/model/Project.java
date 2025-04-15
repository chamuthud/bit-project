package com.residential.construction_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;



@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String projectName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Size(max = 50)
    private String projectType; // e.g., New Build, Renovation

    @Size(max = 30)
    private String status; // e.g., Planning, In Progress, Completed, On Hold

    private LocalDate startDate; // Planned or actual start date

    private LocalDate endDate; // Planned or actual end date

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private User createdBy;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;


    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructor (optional, Lombok handles @NoArgsConstructor)
    public Project(String projectName, String description, String projectType, String status, LocalDate startDate, LocalDate endDate, User createdBy) {
        this.projectName = projectName;
        this.description = description;
        this.projectType = projectType;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }
}
