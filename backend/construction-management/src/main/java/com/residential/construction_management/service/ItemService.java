package com.residential.construction_management.service;

import com.residential.construction_management.dto.request.ItemRequest;
import com.residential.construction_management.dto.response.ItemResponse;
import com.residential.construction_management.exception.ResourceNotFoundException;
import com.residential.construction_management.model.Item;
import com.residential.construction_management.model.User;
import com.residential.construction_management.repository.ItemRepository;
import com.residential.construction_management.repository.UserRepository;
import com.residential.construction_management.security.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;


@Service
public class ItemService {


    @Autowired
    private ItemRepository itemRepository;


    @Autowired
    private UserRepository userRepository; // Needed to fetch current user


    // Helper method to get current authenticated user (ensure they are a VENDOR)
    private User getCurrentVendor() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetailsImpl userDetails)) {
            throw new AccessDeniedException("User details not found in security context.");
        }


        // Ensure the user has the VENDOR role
        boolean isVendor = userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_VENDOR"));


        if (!isVendor) {
            throw new AccessDeniedException("User does not have VENDOR role.");
        }


        // Fetch the full User entity
        return userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userDetails.getId()));
    }


    // Get all items listed by the currently authenticated vendor
    @Transactional(readOnly = true)
    public List<ItemResponse> getMyItems() {
        User currentVendor = getCurrentVendor();
        return itemRepository.findByVendorUserId(currentVendor.getUserId()).stream()
                .map(ItemResponse::fromEntity)
                .collect(Collectors.toList());
    }


    // Get a specific item listed by the currently authenticated vendor
    @Transactional(readOnly = true)
    public ItemResponse getMyItemById(Long itemId) {
        User currentVendor = getCurrentVendor();
        Item item = itemRepository.findByItemIdAndVendorUserId(itemId, currentVendor.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId + " for vendor " + currentVendor.getUserId()));
        return ItemResponse.fromEntity(item);
    }


    // Create a new item for the currently authenticated vendor
    @Transactional
    public ItemResponse createItem(ItemRequest itemRequest) {
        User currentVendor = getCurrentVendor();


        Item item = new Item();
        item.setItemName(itemRequest.getItemName());
        item.setDescription(itemRequest.getDescription());
        item.setUnitPrice(itemRequest.getUnitPrice());
        item.setQtyOnHand(itemRequest.getQtyOnHand());
        item.setUnitOfMeasure(itemRequest.getUnitOfMeasure());
        item.setVendor(currentVendor); // Set the vendor


        Item savedItem = itemRepository.save(item);
        return ItemResponse.fromEntity(savedItem);
    }


    // Update an item owned by the currently authenticated vendor
    @Transactional
    public ItemResponse updateItem(Long itemId, ItemRequest itemRequest) {
        User currentVendor = getCurrentVendor();
        Item item = itemRepository.findByItemIdAndVendorUserId(itemId, currentVendor.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId + " for vendor " + currentVendor.getUserId()));


        // Update fields
        item.setItemName(itemRequest.getItemName());
        item.setDescription(itemRequest.getDescription());
        item.setUnitPrice(itemRequest.getUnitPrice());
        item.setQtyOnHand(itemRequest.getQtyOnHand());
        item.setUnitOfMeasure(itemRequest.getUnitOfMeasure());


        Item updatedItem = itemRepository.save(item);
        return ItemResponse.fromEntity(updatedItem);
    }


    // Delete an item owned by the currently authenticated vendor
    @Transactional
    public void deleteItem(Long itemId) {
        User currentVendor = getCurrentVendor();
        Item item = itemRepository.findByItemIdAndVendorUserId(itemId, currentVendor.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId + " for vendor " + currentVendor.getUserId()));



        itemRepository.delete(item);
    }



}
