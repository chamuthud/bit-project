package com.residential.construction_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal; // Use BigDecimal for currency
import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false)
    private String itemName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2) // Example precision/scale for currency
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be positive")
    private BigDecimal unitPrice;

    @Column(nullable = false)
    @Min(value = 0, message = "Quantity on hand cannot be negative")
    private Integer qtyOnHand; // Matches diagram name

    @Size(max = 50)
    private String unitOfMeasure; // e.g., "piece", "meter", "kg" (Optional but useful)

    // Relationship: Item sold by one Vendor (User)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendor_id", nullable = false)
    private User vendor; // User with ROLE_VENDOR

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;


    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructor (Optional)
    public Item(String itemName, String description, BigDecimal unitPrice, Integer qtyOnHand, String unitOfMeasure, User vendor) {
        this.itemName = itemName;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qtyOnHand = qtyOnHand;
        this.unitOfMeasure = unitOfMeasure;
        this.vendor = vendor;
        this.createdAt = LocalDateTime.now();
    }
}