package com.residential.construction_management.controller;

import com.residential.construction_management.dto.request.ProjectRequest;
import com.residential.construction_management.dto.response.ProjectResponse;
import com.residential.construction_management.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;




    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ProjectResponse>> getAllProjects() {

        List<ProjectResponse> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }


    @GetMapping("/{id}")

    @PreAuthorize("@permissionService.canAccessProject(#projectId, authentication)")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable(value = "id") Long projectId) {
        ProjectResponse project = projectService.getProjectById(projectId);
        return ResponseEntity.ok(project);
    }


    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest projectRequest) {
        ProjectResponse createdProject = projectService.createProject(projectRequest);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    @PreAuthorize("@permissionService.isProjectCreatorOrAdmin(#projectId, authentication)")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable(value = "id") Long projectId,
                                                         @Valid @RequestBody ProjectRequest projectRequest) {
        // Service layer might re-verify ownership, but PreAuthorize is the first gate
        ProjectResponse updatedProject = projectService.updateProject(projectId, projectRequest);
        return ResponseEntity.ok(updatedProject);
    }

    // Delete a project, checking if user is creator or admin
    @DeleteMapping("/{id}")
    @PreAuthorize("@permissionService.isProjectCreatorOrAdmin(#projectId, authentication)")
    public ResponseEntity<?> deleteProject(@PathVariable(value = "id") Long projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok().build();
    }
}
