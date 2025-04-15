package com.residential.construction_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Size(max = 50)
    @Column(unique = true, nullable = false)
    private String username; // Used for login

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false)
    private String password; // Store hashed password

    @NotBlank
    @Size(max = 100)
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Size(max = 15)
    private String nic; // National Identity Card or similar identifier

    @Size(max = 20)
    private String mobile;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false)
    private boolean enabled = true; // For activating/deactivating users

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY) // Eager fetch role for security checks
    @JoinColumn(name = "role_id", nullable = false)
    private UserRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_category_id", nullable = true)
    private VendorCategory vendorCategory;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Item> items = new HashSet<>();


    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public User(String username, String email, String password, String firstName, String lastName, String nic, String mobile, String address) {
        this.username = username;
        this.email = email;
        this.password = password; // Remember to hash this before saving!
        this.firstName = firstName;
        this.lastName = lastName;
        this.nic = nic;
        this.mobile = mobile;
        this.address = address;
        this.enabled = true; // Or false if email verification is needed
        this.createdAt = LocalDateTime.now();
    }
}

