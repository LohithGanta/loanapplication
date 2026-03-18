package com.example.demo.controller;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.entity.AdminModel;
import com.example.demo.service.AdminService;
import com.example.demo.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AdminService adminService;

    public AdminController(AuthenticationManager authenticationManager, JwtService jwtService, AdminService adminService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.adminService = adminService;
    }

    @PostMapping("/admin-login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Login attempt for: " + loginRequest.getEmail());
            System.out.println("Password entered: " + loginRequest.getPassword());
            
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            
            System.out.println("Authentication successful");

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);
            
            System.out.println("Token generated: " + token.substring(0, 20) + "...");

            // Update last login
            adminService.updateLastLogin(loginRequest.getEmail());

            // Get admin details
            AdminModel admin = adminService.getAdminByEmail(loginRequest.getEmail()).orElseThrow();

            LoginResponse response = new LoginResponse(
                    token,
                    admin.getEmail(),
                    admin.getRole(),
                    "Login successful"
            );
            
            System.out.println("Sending response: " + response.getMessage());

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            System.out.println("Bad credentials for: " + loginRequest.getEmail());
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("message", "An error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/hash-password")
    public ResponseEntity<?> hashPassword(@RequestParam String password) {
        org.springframework.security.crypto.password.PasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        String hashed = encoder.encode(password);
        Map<String, String> response = new HashMap<>();
        response.put("password", password);
        response.put("hash", hashed);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            org.springframework.web.client.RestTemplate restTemplate = new org.springframework.web.client.RestTemplate();
            Object users = restTemplate.getForObject("http://localhost:8082/api/user/all", Object.class);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.ok(new java.util.ArrayList<>());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        String email = principal.getName();
        AdminModel admin = adminService.getAdminByEmail(email)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
        
        Map<String, Object> profile = new HashMap<>();
        profile.put("id", admin.getId());
        profile.put("firstName", admin.getFirstName());
        profile.put("lastName", admin.getLastName());
        profile.put("email", admin.getEmail());
        profile.put("role", admin.getRole());
        profile.put("isActive", admin.getIsActive());
        profile.put("createdAt", admin.getCreatedAt());
        profile.put("lastLogin", admin.getLastLogin());
        
        return ResponseEntity.ok(profile);
    }
}
