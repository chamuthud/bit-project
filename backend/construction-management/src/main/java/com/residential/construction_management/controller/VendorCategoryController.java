package com.residential.construction_management.controller;

import com.residential.construction_management.dto.request.VendorCategoryRequest;
import com.residential.construction_management.dto.response.VendorCategoryResponse;
import com.residential.construction_management.service.VendorCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/vendor-categories") // Base path for categories
public class VendorCategoryController {

    @Autowired
    private VendorCategoryService categoryService;

    // Publicly accessible endpoint to get all categories
    @GetMapping
    public ResponseEntity<List<VendorCategoryResponse>> getAllCategories() {
        List<VendorCategoryResponse> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // Admin-only endpoint to create a category
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorCategoryResponse> createCategory(@Valid @RequestBody VendorCategoryRequest categoryRequest) {
        VendorCategoryResponse createdCategory = categoryService.createCategory(categoryRequest);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    // Admin-only endpoint to update a category
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorCategoryResponse> updateCategory(@PathVariable Integer id, @Valid @RequestBody VendorCategoryRequest categoryRequest) {
        VendorCategoryResponse updatedCategory = categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok(updatedCategory);
    }

    // Admin-only endpoint to delete a category
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}
