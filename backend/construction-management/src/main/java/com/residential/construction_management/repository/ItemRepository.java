package com.residential.construction_management.repository;

import com.residential.construction_management.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    // Find all items sold by a specific vendor
    List<Item> findByVendorUserId(Long vendorId);

    // Find a specific item sold by a specific vendor
    Optional<Item> findByItemIdAndVendorUserId(Long itemId, Long vendorId);

    // Add other queries if needed (e.g., search by name)
    // List<Item> findByItemNameContainingIgnoreCase(String name);
}
