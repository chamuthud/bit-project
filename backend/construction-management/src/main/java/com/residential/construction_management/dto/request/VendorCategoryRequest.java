package com.residential.construction_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VendorCategoryRequest {
    @NotBlank(message = "Category name cannot be blank")
    @Size(max = 100)
    private String name;
}
