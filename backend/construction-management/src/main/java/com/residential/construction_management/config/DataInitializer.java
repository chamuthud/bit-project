package com.residential.construction_management.config;

import com.residential.construction_management.model.User;
import com.residential.construction_management.model.UserRole;
import com.residential.construction_management.repository.UserRepository;
import com.residential.construction_management.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder; // Import PasswordEncoder
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired // Use Autowired for dependencies
    private UserRoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Inject PasswordEncoder

    // Define role names constants
    public static final String ROLE_CLIENT = "ROLE_CLIENT";
    public static final String ROLE_CONTRACTOR = "ROLE_CONTRACTOR";
    public static final String ROLE_VENDOR = "ROLE_VENDOR";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeAdminUser(); // Call admin user initialization
        // Add sample project/task creation here if needed for testing
    }

    private void initializeRoles() {
        List<String> roleNames = Arrays.asList(ROLE_CLIENT, ROLE_CONTRACTOR, ROLE_VENDOR, ROLE_ADMIN);

        for (String roleName : roleNames) {
            if (roleRepository.findByRoleName(roleName).isEmpty()) {
                roleRepository.save(new UserRole(roleName));
                System.out.println("Created role: " + roleName);
            }
        }
    }

    // Method to create a default admin user if one doesn't exist
    private void initializeAdminUser() {
        String adminUsername = "admin";
        String adminEmail = "admin@example.com"; // Change as needed

        if (!userRepository.existsByUsername(adminUsername) && !userRepository.existsByEmail(adminEmail)) {
            UserRole adminRole = roleRepository.findByRoleName(ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Admin Role is not found. Cannot create admin user."));

            User adminUser = new User();
            adminUser.setUsername(adminUsername);
            adminUser.setEmail(adminEmail);
            // Encode the default password
            adminUser.setPassword(passwordEncoder.encode("password")); // Change default password!
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setEnabled(true);
            adminUser.setRole(adminRole);

            userRepository.save(adminUser);
            System.out.println("Created default admin user: " + adminUsername);
        }
    }
}



