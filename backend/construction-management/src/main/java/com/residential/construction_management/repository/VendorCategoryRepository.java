package com.residential.construction_management.repository;

import com.residential.construction_management.model.VendorCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VendorCategoryRepository extends JpaRepository<VendorCategory, Integer> {
    Optional<VendorCategory> findByName(String name);
    boolean existsByName(String name);
}
