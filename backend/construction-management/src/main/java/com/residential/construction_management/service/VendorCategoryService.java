package com.residential.construction_management.service;

import com.residential.construction_management.dto.request.VendorCategoryRequest;
import com.residential.construction_management.dto.response.VendorCategoryResponse;
import com.residential.construction_management.exception.ResourceAlreadyExistsException;
import com.residential.construction_management.exception.ResourceNotFoundException;
import com.residential.construction_management.model.VendorCategory;
import com.residential.construction_management.repository.VendorCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VendorCategoryService {

    @Autowired
    private VendorCategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<VendorCategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(VendorCategoryResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VendorCategoryResponse getCategoryById(Integer categoryId) {
        VendorCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("VendorCategory", "id", categoryId));
        return VendorCategoryResponse.fromEntity(category);
    }

    @Transactional
    public VendorCategoryResponse createCategory(VendorCategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new ResourceAlreadyExistsException("VendorCategory", "name", categoryRequest.getName());
        }
        VendorCategory category = new VendorCategory(categoryRequest.getName());
        VendorCategory savedCategory = categoryRepository.save(category);
        return VendorCategoryResponse.fromEntity(savedCategory);
    }

    @Transactional
    public VendorCategoryResponse updateCategory(Integer categoryId, VendorCategoryRequest categoryRequest) {
        VendorCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("VendorCategory", "id", categoryId));

        // Check if new name conflicts with another existing category
        categoryRepository.findByName(categoryRequest.getName()).ifPresent(existingCategory -> {
            if (!existingCategory.getCategoryId().equals(categoryId)) {
                throw new ResourceAlreadyExistsException("VendorCategory", "name", categoryRequest.getName());
            }
        });

        category.setName(categoryRequest.getName());
        VendorCategory updatedCategory = categoryRepository.save(category);
        return VendorCategoryResponse.fromEntity(updatedCategory);
    }

    @Transactional
    public void deleteCategory(Integer categoryId) {
        VendorCategory category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("VendorCategory", "id", categoryId));

        categoryRepository.delete(category);
    }
}
