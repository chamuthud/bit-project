package com.residential.construction_management.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_roles")
@Getter
@Setter
@NoArgsConstructor
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Column(length = 50, unique = true, nullable = false)
    private String roleName; // e.g., ROLE_CLIENT, ROLE_CONTRACTOR, ROLE_VENDOR, ROLE_ADMIN

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();



    public UserRole(String roleName) {
        this.roleName = roleName;
    }
}
