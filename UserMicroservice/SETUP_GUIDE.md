# UserMicroservice Setup Guide

## Overview
Complete user management microservice with registration, login, and dashboard.

## Architecture
- **Port**: 8082
- **Database**: user_db (MySQL)
- **Authentication**: JWT (same secret as AdminMicroservice)
- **Features**: User registration, login, profile, dashboard

## Database Setup

```sql
CREATE DATABASE IF NOT EXISTS user_db;
USE user_db;

-- Table will be auto-created by Hibernate
-- Manual user creation for testing:
INSERT INTO users (first_name, last_name, email, password, phone, is_active, created_at) 
VALUES ('Test', 'User', 'user@test.com', '$2a$10$HASH_HERE', '1234567890', true, NOW());
```

## Endpoints

### Public Endpoints
- GET `/` - Home/Landing page
- GET `/register` - Registration page
- POST `/api/auth/register` - Register new user
- GET `/login` - Login page
- POST `/api/auth/login` - User login

### Protected Endpoints (Requires JWT)
- GET `/dashboard` - User dashboard
- GET `/api/user/profile` - Get user profile
- PUT `/api/user/profile` - Update profile

## Files Created

### Core Files
1. UserMicroserviceApplication.java - Main application
2. application.yml - Configuration (port 8082, user_db)
3. pom.xml - Dependencies

### Entity Layer
4. User.java - User entity (id, firstName, lastName, email, password, phone, isActive, createdAt, lastLogin)

### Repository Layer
5. UserRepository.java - JPA repository

### Service Layer
6. UserService.java - User CRUD operations, implements UserDetailsService
7. JwtService.java - JWT token generation/validation

### Controller Layer
8. AuthController.java - Registration & login APIs
9. UserController.java - User profile APIs
10. ViewController.java - Serve HTML pages

### Security Layer
11. SecurityConfig.java - Spring Security configuration
12. JwtAuthenticationFilter.java - JWT validation filter

### DTOs
13. RegisterRequest.java - Registration data
14. LoginRequest.java - Login credentials
15. LoginResponse.java - JWT token response
16. UserProfileResponse.java - User profile data

### Web Pages (Thymeleaf)
17. register.html - User registration form
18. login.html - User login form
19. dashboard.html - User dashboard

## Startup Commands

```bash
# Terminal 1 - Service Registry
cd ServiceRegistry
..\mvnw.cmd spring-boot:run

# Terminal 2 - AdminMicroservice  
cd AdminMicroservice
..\mvnw.cmd spring-boot:run

# Terminal 3 - UserMicroservice
cd UserMicroservice
..\mvnw.cmd spring-boot:run
```

## Testing Flow

1. **Register**: http://localhost:8082/register
   - Fill form with user details
   - Submit to create account

2. **Login**: http://localhost:8082/login
   - Email: user@test.com
   - Password: user123
   - Redirects to dashboard

3. **Dashboard**: http://localhost:8082/dashboard
   - Shows user profile
   - Protected by JWT

4. **Admin View Users**: http://localhost:8081/admin/users (to be created)
   - Admin can see all registered users

## Integration with AdminMicroservice

AdminMicroservice will need:
- REST client to call UserMicroservice API
- Admin page to list all users
- Endpoint: GET http://localhost:8082/api/admin/users (with admin JWT)

## Next Steps After Creation

1. Build project: `mvnw clean install`
2. Create user_db database in MySQL
3. Start all 3 services
4. Test registration and login
5. Add admin user management page
