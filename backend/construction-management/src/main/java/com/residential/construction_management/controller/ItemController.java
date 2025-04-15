package com.residential.construction_management.controller;

import com.residential.construction_management.dto.request.ItemRequest;
import com.residential.construction_management.dto.response.ItemResponse;
import com.residential.construction_management.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/vendor/items") // Scoped under '/vendor' for items managed by vendors
public class ItemController {

    @Autowired
    private ItemService itemService;

    // Endpoint for vendors to get their own items
    @GetMapping("/my")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<List<ItemResponse>> getMyItems() {
        List<ItemResponse> items = itemService.getMyItems();
        return ResponseEntity.ok(items);
    }

    // Endpoint for vendors to get a specific one of their items
    @GetMapping("/my/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<ItemResponse> getMyItemById(@PathVariable Long id) {
        ItemResponse item = itemService.getMyItemById(id);
        return ResponseEntity.ok(item);
    }


    // Endpoint for vendors to create a new item
    @PostMapping
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<ItemResponse> createItem(@Valid @RequestBody ItemRequest itemRequest) {
        ItemResponse createdItem = itemService.createItem(itemRequest);
        return new ResponseEntity<>(createdItem, HttpStatus.CREATED);
    }

    // Endpoint for vendors to update their item
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<ItemResponse> updateItem(@PathVariable Long id, @Valid @RequestBody ItemRequest itemRequest) {
        // Service layer ensures only the owner vendor can update
        ItemResponse updatedItem = itemService.updateItem(id, itemRequest);
        return ResponseEntity.ok(updatedItem);
    }

    // Endpoint for vendors to delete their item
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('VENDOR')")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        // Service layer ensures only the owner vendor can delete
        itemService.deleteItem(id);
        return ResponseEntity.ok().build();
    }



}
