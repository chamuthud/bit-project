package com.residential.construction_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vendor_categories")
@Getter
@Setter
@NoArgsConstructor
public class VendorCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, unique = true)
    private String name; // e.g., Plumbing Supplies, Electrical, Lumber

    // Relationship: One category can have many vendors
    // MappedBy refers to the 'vendorCategory' field in the User entity
    @OneToMany(mappedBy = "vendorCategory", fetch = FetchType.LAZY)
    private Set<User> vendors = new HashSet<>();

    public VendorCategory(String name) {
        this.name = name;
    }
}
