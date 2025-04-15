package com.residential.construction_management.repository;

import com.residential.construction_management.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Find projects created by a specific user
    List<Project> findByCreatedByUserId(Long userId);

    // Add other custom query methods if needed
    // List<Project> findByStatus(String status);
}
