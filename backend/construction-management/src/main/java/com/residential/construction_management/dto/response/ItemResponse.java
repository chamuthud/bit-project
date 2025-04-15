package com.residential.construction_management.dto.response;

import com.residential.construction_management.model.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ItemResponse {
    private Long itemId;
    private String itemName;
    private String description;
    private BigDecimal unitPrice;
    private Integer qtyOnHand;
    private String unitOfMeasure;
    private Long vendorId;
    private String vendorUsername; // Assuming User has getUsername
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ItemResponse fromEntity(Item item) {
        ItemResponse dto = new ItemResponse();
        dto.setItemId(item.getItemId());
        dto.setItemName(item.getItemName());
        dto.setDescription(item.getDescription());
        dto.setUnitPrice(item.getUnitPrice());
        dto.setQtyOnHand(item.getQtyOnHand());
        dto.setUnitOfMeasure(item.getUnitOfMeasure());
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        if (item.getVendor() != null) {
            dto.setVendorId(item.getVendor().getUserId());
            dto.setVendorUsername(item.getVendor().getUsername());
        }
        return dto;
    }
}
