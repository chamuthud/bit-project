package com.residential.construction_management.controller;

import com.residential.construction_management.config.DataInitializer;
import com.residential.construction_management.dto.request.LoginRequest;
import com.residential.construction_management.dto.request.SignupRequest;
import com.residential.construction_management.dto.response.JwtResponse;
import com.residential.construction_management.dto.response.MessageResponse;
import com.residential.construction_management.model.User;
import com.residential.construction_management.model.UserRole;
import com.residential.construction_management.repository.UserRepository;
import com.residential.construction_management.repository.UserRoleRepository;
import com.residential.construction_management.security.jwt.JwtUtils;
import com.residential.construction_management.security.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600) // Allow requests from any origin (adjust for production)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        // Assuming UserDetailsImpl has a method to get the single role string
        String role = userDetails.getAuthorities().iterator().next().getAuthority();


        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                role));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), // Encode password
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getNic(),
                signUpRequest.getMobile(),
                signUpRequest.getAddress());

        // --- MODIFIED ROLE ASSIGNMENT ---
        String requestedRole = signUpRequest.getRole(); // Get role from request
        String roleNameToFind;

        // Map frontend role string to backend role constant
        switch (requestedRole.toLowerCase()) {
            case "client":
                roleNameToFind = DataInitializer.ROLE_CLIENT;
                break;
            case "contractor":
                roleNameToFind = DataInitializer.ROLE_CONTRACTOR;
                break;
            case "vendor":
                roleNameToFind = DataInitializer.ROLE_VENDOR;
                break;
            default:
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Invalid role specified. Valid roles are 'client', 'contractor', 'vendor'."));
        }

        UserRole userRole = roleRepository.findByRoleName(roleNameToFind)
                .orElseThrow(() -> new RuntimeException("Error: Role '" + roleNameToFind + "' is not found in database. Initialization might be needed."));
        user.setRole(userRole);
        // --- End MODIFIED ROLE ASSIGNMENT ---


        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}