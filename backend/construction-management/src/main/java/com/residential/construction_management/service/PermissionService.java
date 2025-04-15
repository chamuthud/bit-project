package com.residential.construction_management.service;

import com.residential.construction_management.exception.ResourceNotFoundException;

import com.residential.construction_management.model.Project;
import com.residential.construction_management.model.Task;
import com.residential.construction_management.model.User;
import com.residential.construction_management.repository.ProjectRepository;
import com.residential.construction_management.repository.TaskRepository;
import com.residential.construction_management.security.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("permissionService") // Give the bean a specific name for SpEL expressions
public class PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);


    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;




    @Transactional(readOnly = true) // Ensure transaction context for lazy loading if needed
    public boolean isProjectCreatorOrAdmin(Long projectId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Allow ADMINs unrestricted access (adjust role name if different)
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return true;
        }


        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl userDetails)) {
            logger.warn("Principal is not an instance of UserDetailsImpl: {}", principal.getClass().getName());
            return false; // Should not happen with our setup, but good practice
        }


        Long currentUserId = userDetails.getId();


        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));


        // Check if the project's creator ID matches the authenticated user's ID
        boolean isCreator = project.getCreatedBy() != null && project.getCreatedBy().getUserId().equals(currentUserId);


        if (!isCreator) {
            logger.warn("Permission denied: User {} is not the creator of project {}", currentUserId, projectId);
        }


        return isCreator;
    }



    @Transactional(readOnly = true)
    public boolean canAccessProject(Long projectId, Authentication authentication) {
        // TODO: Implement more complex logic later if ProjectUser relationship is added
        // For now, anyone who created it or is an admin can access
        return isProjectCreatorOrAdmin(projectId, authentication);
    }


    // --- Task Permissions ---



    @Transactional(readOnly = true)
    public boolean canAccessTask(Long projectId, Long taskId, Authentication authentication) {
        // Basic check: Can the user access the parent project?
        if (!canAccessProject(projectId, authentication)) {
            logger.warn("Permission denied for task {}: User cannot access parent project {}", taskId, projectId);
            return false;
        }



        return true;
    }



    @Transactional(readOnly = true)
    public boolean canModifyTasksInProject(Long projectId, Authentication authentication) {

        return isProjectCreatorOrAdmin(projectId, authentication);
    }
}