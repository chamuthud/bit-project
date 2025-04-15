package com.residential.construction_management.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ItemRequest {

    @NotBlank(message = "Item name cannot be blank")
    @Size(max = 150)
    private String itemName;

    private String description;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be positive")
    private BigDecimal unitPrice;

    @NotNull(message = "Quantity on hand is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer qtyOnHand;

    @Size(max = 50)
    private String unitOfMeasure;

    // Vendor ID will be determined from the authenticated user in the service
}