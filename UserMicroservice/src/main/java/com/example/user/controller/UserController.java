package com.example.user.controller;

import com.example.user.entity.User;
import com.example.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(Map.of(
            "firstName", user.getFirstName(),
            "lastName", user.getLastName(),
            "email", user.getEmail(),
            "phone", user.getPhone() != null ? user.getPhone() : "",
            "createdAt", user.getCreatedAt().toString()
        ));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers().stream().map(u -> Map.of(
            "id", u.getId(),
            "firstName", u.getFirstName(),
            "lastName", u.getLastName(),
            "email", u.getEmail(),
            "phone", u.getPhone() != null ? u.getPhone() : "",
            "isActive", u.getIsActive(),
            "createdAt", u.getCreatedAt().toString()
        )).toList());
    }
}
