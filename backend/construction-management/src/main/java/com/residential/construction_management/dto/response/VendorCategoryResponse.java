package com.residential.construction_management.dto.response;

import com.residential.construction_management.model.VendorCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VendorCategoryResponse {
    private Integer categoryId;
    private String name;

    public static VendorCategoryResponse fromEntity(VendorCategory category) {
        VendorCategoryResponse dto = new VendorCategoryResponse();
        dto.setCategoryId(category.getCategoryId());
        dto.setName(category.getName());
        return dto;
    }
}
