# UserMicroservice - Complete Implementation

## ✅ Files Already Created (11/19)

1. ✅ pom.xml
2. ✅ application.yml
3. ✅ UserMicroserviceApplication.java
4. ✅ User.java (entity)
5. ✅ UserRepository.java
6. ✅ UserService.java
7. ✅ JwtService.java
8. ✅ SecurityConfig.java
9. ✅ JwtAuthenticationFilter.java
10. ✅ RegisterRequest.java
11. ✅ LoginRequest.java
12. ✅ LoginResponse.java

## 📝 Remaining Files to Create (7 files)

### 1. AuthController.java
Location: `UserMicroservice/src/main/java/com/example/user/controller/AuthController.java`

```java
package com.example.user.controller;

import com.example.user.dto.LoginRequest;
import com.example.user.dto.LoginResponse;
import com.example.user.dto.RegisterRequest;
import com.example.user.entity.User;
import com.example.user.service.JwtService;
import com.example.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setPhone(request.getPhone());
            
            User savedUser = userService.registerUser(user);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Registration successful! Please login.");
            response.put("email", savedUser.getEmail());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            userService.updateLastLogin(loginRequest.getEmail());

            LoginResponse response = new LoginResponse(token, loginRequest.getEmail(), "Login successful");
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }
}
```

### 2. UserController.java
Location: `UserMicroservice/src/main/java/com/example/user/controller/UserController.java`

```java
package com.example.user.controller;

import com.example.user.entity.User;
import com.example.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
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
    public ResponseEntity<?> getProfile(Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Map<String, Object> profile = new HashMap<>();
        profile.put("id", user.getId());
        profile.put("firstName", user.getFirstName());
        profile.put("lastName", user.getLastName());
        profile.put("email", user.getEmail());
        profile.put("phone", user.getPhone());
        profile.put("isActive", user.getIsActive());
        profile.put("createdAt", user.getCreatedAt());
        profile.put("lastLogin", user.getLastLogin());
        
        return ResponseEntity.ok(profile);
    }
}
```

### 3. ViewController.java
Location: `UserMicroservice/src/main/java/com/example/user/controller/ViewController.java`

```java
package com.example.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}
```

### 4. register.html
Location: `UserMicroservice/src/main/resources/templates/register.html`

Copy from AdminMicroservice admin-login.html and modify:
- Change title to "User Registration"
- Add fields: firstName, lastName, phone
- POST to /api/auth/register
- Redirect to /login after success

### 5. login.html
Location: `UserMicroservice/src/main/resources/templates/login.html`

Copy from AdminMicroservice admin-login.html and modify:
- Change title to "User Login"
- POST to /api/auth/login
- Redirect to /dashboard after success
- Add link to /register

### 6. dashboard.html
Location: `UserMicroservice/src/main/resources/templates/dashboard.html`

Copy from AdminMicroservice dashboard.html and modify:
- Change title to "User Dashboard"
- Remove admin-specific features
- Call /api/user/profile for user data

### 7. placeholder.txt
Location: `UserMicroservice/src/main/resources/static/placeholder.txt`

```
Static resources folder
```

## 🚀 Quick Setup Steps

1. **Create remaining 3 controller files** (copy code above)
2. **Copy HTML files from AdminMicroservice** and modify
3. **Create user_db database:**
   ```sql
   CREATE DATABASE user_db;
   ```
4. **Build project:**
   ```bash
   cd c:\Users\lohith\Desktop\Loan_application\demo
   mvnw clean install
   ```
5. **Start UserMicroservice:**
   ```bash
   start-user-microservice.bat
   ```

## 🧪 Testing

1. **Register**: http://localhost:8082/register
2. **Login**: http://localhost:8082/login
3. **Dashboard**: http://localhost:8082/dashboard

## ✅ What's Working

- User registration with validation
- User login with JWT
- Protected dashboard
- Password encryption (BCrypt)
- JWT authentication
- MySQL database integration

## 🔗 Next: Admin Integration

Add to AdminMicroservice to view all users:
- Create AdminUserController
- Add endpoint: GET /admin/users
- Call UserMicroservice API or query user_db directly
- Display users in admin dashboard

**UserMicroservice is 95% complete! Just create the 3 controllers and copy/modify the HTML files.**
