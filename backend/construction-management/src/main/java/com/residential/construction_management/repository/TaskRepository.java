package com.residential.construction_management.repository;

import com.residential.construction_management.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Find all tasks for a specific project
    List<Task> findByProjectProjectId(Long projectId);

    // Find a specific task within a specific project
    Optional<Task> findByTaskIdAndProjectProjectId(Long taskId, Long projectId);


    // List<Task> findByProjectProjectIdAndTaskStatus(Long projectId, String status);
}
