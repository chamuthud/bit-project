package com.residential.construction_management.service;

import com.residential.construction_management.dto.request.ProjectRequest;
import com.residential.construction_management.dto.response.ProjectResponse;
import com.residential.construction_management.exception.ResourceNotFoundException;

import com.residential.construction_management.model.Project;
import com.residential.construction_management.model.User;
import com.residential.construction_management.repository.ProjectRepository;
import com.residential.construction_management.repository.UserRepository;
import com.residential.construction_management.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository; // Inject UserRepository


    // Helper method to get the current authenticated user
    private User getCurrentUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Fetch the full User entity using the ID from UserDetailsImpl
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userDetails.getId()));
    }


    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {

        User currentUser = getCurrentUser();
        return projectRepository.findByCreatedByUserId(currentUser.getUserId()).stream()
                .map(ProjectResponse::fromEntity)
                .collect(Collectors.toList());

    }


    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));




        return ProjectResponse.fromEntity(project);
    }


    @Transactional
    public ProjectResponse createProject(ProjectRequest projectRequest) {
        User currentUser = getCurrentUser();


        Project project = new Project();
        project.setProjectName(projectRequest.getProjectName());
        project.setDescription(projectRequest.getDescription());
        project.setProjectType(projectRequest.getProjectType());
        project.setStatus(projectRequest.getStatus()); // Consider setting a default status like "Planning"
        project.setStartDate(projectRequest.getStartDate());
        project.setEndDate(projectRequest.getEndDate());
        project.setCreatedBy(currentUser); // Set the creator


        Project savedProject = projectRepository.save(project);
        return ProjectResponse.fromEntity(savedProject);
    }


    @Transactional
    public ProjectResponse updateProject(Long projectId, ProjectRequest projectRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));


        // Optional: Add authorization check - ensure the current user can update this project
        User currentUser = getCurrentUser();
        if (!project.getCreatedBy().getUserId().equals(currentUser.getUserId())) {

            throw new RuntimeException("Error: You don't have permission to update this project.");
        }


        project.setProjectName(projectRequest.getProjectName());
        project.setDescription(projectRequest.getDescription());
        project.setProjectType(projectRequest.getProjectType());
        project.setStatus(projectRequest.getStatus());
        project.setStartDate(projectRequest.getStartDate());
        project.setEndDate(projectRequest.getEndDate());
        // createdBy should not change on update


        Project updatedProject = projectRepository.save(project);
        return ProjectResponse.fromEntity(updatedProject);
    }


    @Transactional
    public void deleteProject(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));



        User currentUser = getCurrentUser();
        if (!project.getCreatedBy().getUserId().equals(currentUser.getUserId())) {

            throw new RuntimeException("Error: You don't have permission to delete this project.");
        }


        projectRepository.delete(project);
    }
}
