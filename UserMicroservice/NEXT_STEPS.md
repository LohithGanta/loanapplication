# UserMicroservice - Next Steps Summary

## ✅ What's Already Created

1. **Project Structure** - All folders created
2. **pom.xml** - Dependencies configured (Spring Boot, Security, JWT, MySQL, Thymeleaf)
3. **application.yml** - Port 8082, user_db database, JWT config
4. **UserMicroserviceApplication.java** - Main class with password hash generator
5. **User.java** - Entity with id, firstName, lastName, email, password, phone, isActive, createdAt, lastLogin
6. **start-user-microservice.bat** - Startup script
7. **SETUP_GUIDE.md** - Complete documentation

## 📋 What You Need to Do Next

### Step 1: Copy Files from AdminMicroservice (Fastest Way)

Since UserMicroservice uses the same JWT authentication pattern, copy these files from AdminMicroservice and rename classes:

**Copy & Modify:**
```
AdminMicroservice/config/JwtService.java 
  → UserMicroservice/config/JwtService.java (no changes needed)

AdminMicroservice/config/JwtAuthenticationFilter.java 
  → UserMicroservice/config/JwtAuthenticationFilter.java (no changes needed)

AdminMicroservice/config/SecurityConfig.java 
  → UserMicroservice/config/SecurityConfig.java 
  (change permitAll paths to: /register, /login, /, /api/auth/*)
```

### Step 2: Create Repository

**File:** `UserMicroservice/src/main/java/com/example/user/repository/UserRepository.java`

```java
package com.example.user.repository;

import com.example.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

### Step 3: Create UserService

**File:** `UserMicroservice/src/main/java/com/example/user/service/UserService.java`

```java
package com.example.user.service;

import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void updateLastLogin(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        });
    }
}
```

### Step 4: Create DTOs

Create 3 simple DTO classes in `dto` folder - copy from AdminMicroservice and reuse.

### Step 5: Create Controllers

**AuthController** - Handle /api/auth/register and /api/auth/login
**UserController** - Handle /api/user/profile
**ViewController** - Serve HTML pages (/, /register, /login, /dashboard)

### Step 6: Create HTML Pages

Copy `admin-login.html` and `dashboard.html` from AdminMicroservice, modify for user registration/login.

### Step 7: Database Setup

```sql
CREATE DATABASE user_db;
```

### Step 8: Build & Run

```bash
cd c:\Users\lohith\Desktop\Loan_application\demo
mvnw clean install
start-user-microservice.bat
```

## 🚀 Quick Start (Minimal Viable Product)

**Option: Use AdminMicroservice as Template**

1. Copy entire AdminMicroservice folder
2. Rename to UserMicroservice
3. Change:
   - Port: 8081 → 8082
   - Database: admin_db → user_db
   - Table: admin_users → users
   - Package: com.example.demo → com.example.user
   - Class names: AdminModel → User, AdminService → UserService
4. Remove admin-specific features
5. Add registration endpoint

This is the FASTEST way to get UserMicroservice working!

## 📞 What to Ask Me Next

1. **"Copy AdminMicroservice files to UserMicroservice"** - I'll do the copying and renaming
2. **"Create remaining files one by one"** - I'll create each file
3. **"Show me the registration page HTML"** - I'll create the UI
4. **"How to integrate with AdminMicroservice"** - I'll show the integration

## 🎯 Current Status

- **AdminMicroservice**: ✅ Fully working (login, dashboard, JWT)
- **UserMicroservice**: 🟡 30% complete (structure, entity, config ready)
- **Integration**: ❌ Not started (admin viewing users)

**Recommendation:** Copy AdminMicroservice structure to UserMicroservice, then modify. This will save 80% of the work!
