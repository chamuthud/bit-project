package com.residential.construction_management.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set; // Keep this if you plan to allow role selection during signup

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(max = 100)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 120) // Enforce a minimum password length
    private String password;

    // Include other fields from User entity as needed for registration
    @NotBlank
    @Size(max = 50)
    private String firstName;

    @NotBlank
    @Size(max = 50)
    private String lastName;

    @Size(max = 15)
    private String nic;

    @Size(max = 20)
    private String mobile;

    private String address;

    @NotBlank(message = "Role selection is required")
    private String role;




}
