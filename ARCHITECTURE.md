# Loan Application - Architecture & Request Flow Guide

## 🏛️ System Architecture

### High-Level Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                         CLIENT LAYER                                │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  Web Browser (HTML/CSS/JavaScript)                           │  │
│  │  - http://localhost:8080/customer-dashboard                  │  │
│  │  - http://localhost:8081/admin-login                         │  │
│  └────────────────┬─────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────┘
                     │ HTTP/REST Requests
                     ↓
┌─────────────────────────────────────────────────────────────────────┐
│                      API GATEWAY LAYER (Port 8080)                  │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  Spring Cloud Gateway / API Gateway                          │  │
│  │  - Request routing                                           │  │
│  │  - Load balancing                                            │  │
│  │  - Security headers                                          │  │
│  │  - Request/Response logging                                  │  │
│  └────────────────┬──────────────────────────────────────────────┘  │
└─────────┬───────────────────────────────┬──────────────────────────┘
          │                               │
          │ Route: /admin/**              │ Route: /loan/**
          ↓                               ↓
┌──────────────────────────┐  ┌──────────────────────────┐
│   ADMIN MICROSERVICE     │  │  LOAN MICROSERVICE       │
│   (Port 8081)            │  │  (Port 8082)             │
│                          │  │                          │
│ Endpoints:               │  │ Endpoints:               │
│ - POST /admin-login      │  │ - POST /loan/apply       │
│ - GET /admin-dashboard   │  │ - GET /loan/status       │
│ - PUT /admin/profile     │  │ - PUT /loan/{id}         │
│                          │  │ - DELETE /loan/{id}      │
└────────┬─────────────────┘  └────────┬─────────────────┘
         │                             │
         └──────────────┬──────────────┘
                        │ Service Discovery
                        ↓
┌─────────────────────────────────────────────────────────────────────┐
│              SERVICE REGISTRY / EUREKA (Port 8761)                  │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  - Service Discovery & Registration                          │  │
│  │  - Health Checks                                             │  │
│  │  - Load Balancing Metadata                                   │  │
│  │  - Client-side Routing                                       │  │
│  └────────────────┬─────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────┘
                        │ JDBC Connections
                        ↓
┌─────────────────────────────────────────────────────────────────────┐
│                   DATA PERSISTENCE LAYER                            │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │  Spring Data JPA / Hibernate ORM                             │  │
│  │  - Entity mapping                                            │  │
│  │  - Query generation                                          │  │
│  │  - Transaction management                                    │  │
│  └────────────────┬─────────────────────────────────────────────┘  │
│                   │ HikariCP Connection Pool                        │
│  ┌────────────────┴─────────────────────────────────────────────┐  │
│  │  MySQL Database (Port 3306)                                  │  │
│  │  Database: admin_db                                          │  │
│  │                                                               │  │
│  │  Tables:                                                     │  │
│  │  - admin_users (id, email, password, role, status)          │  │
│  │  - loans (id, user_id, amount, status, created_at)          │  │
│  │  - loan_applications (id, user_id, amount, status)          │  │
│  └──────────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Request Flow - Detailed Step-by-Step

### Scenario 1: Admin Login Flow

```
1. User Action
   └─> Open http://localhost:8081/admin-login in browser
   
2. Frontend Layer
   └─> Load admin-login.html
   └─> Display login form (email, password)
   
3. User Submits Form
   └─> POST /admin-login
   └─> Payload: { email: "admin@example.com", password: "admin123" }
   
4. API Gateway (8080) receives request
   └─> Route rules check: is it /admin/* ?
   └─> YES → Route to AdminMicroservice (8081)
   
5. AdminMicroservice receives request
   └─> Route: POST /admin-login
   └─> Handler: AdminController.login()
   
6. Authentication Processing
   └─> Extract email & password from request
   └─> Call: AdminService.authenticate(email, password)
   
7. Database Query
   └─> SELECT * FROM admin_users WHERE email = 'admin@example.com'
   └─> HikariCP → MySQL connection
   
8. Password Validation
   └─> Fetch admin_users record
   └─> Compare stored hash with input using BCrypt
   
9. Token Generation
   └─> If password matches:
   └─> Generate JWT token using JwtService
   └─> Token expires in 900000ms (15 minutes)
   
10. Response to Client
    └─> HTTP 200 OK
    └─> Response: { token: "eyJhbGc...", user: { id, email, role } }
    
11. Frontend Stores Token
    └─> Save to localStorage or sessionStorage
    └─> Store in browser cookie (if HttpOnly enabled)
    
12. Redirect
    └─> Redirect to /admin-dashboard
    └─> Pass token in Authorization header for subsequent requests
```

### Scenario 2: Loan Application Flow (through API Gateway)

```
1. User Action
   └─> Navigate to http://localhost:8080/customer-dashboard
   └─> Click "Apply for Loan" button
   
2. API Gateway (8080) receives request
   └─> GET /customer-dashboard
   └─> Route check: /loan/* or /customer/*
   └─> Route to appropriate microservice
   
3. LoanMicroservice Processing
   └─> Handler: LoanController.applyForLoan()
   └─> Extract from request: amount, tenure, loanType
   
4. Business Logic
   └─> Validate input:
      - Amount between min/max limits
      - User has verified account
      - User doesn't have pending application
   └─> Calculate EMI
   └─> Determine interest rate based on loan type
   
5. Database Write
   └─> INSERT into loans table:
      ```sql
      INSERT INTO loans (user_id, amount, interest_rate, tenure, status, created_at)
      VALUES (123, 50000, 7.5, 12, 'PENDING', NOW())
      ```
   └─> HikariCP → MySQL connection (write operation)
   
6. Response
   └─> HTTP 201 CREATED
   └─> Response: { loanId: 456, status: 'PENDING', amount: 50000, ... }
   
7. Service Registration Update
   └─> LoanMicroservice notifies Eureka:
      "I handled this loan application"
   └─> Update load balancing metrics
```

### Scenario 3: Cross-Service Request (Service-to-Service)

```
1. Admin View All Loans
   └─> GET /admin/dashboard/all-loans
   └─> Sent to AdminMicroservice (8081)
   
2. AdminService needs loan data
   └─> Should call LoanMicroservice
   └─> But where is LoanMicroservice?
   
3. Service Discovery via Eureka
   └─> AdminService queries Eureka (8761)
   └─> "Where is the LOANMICROSERVICE instance?"
   └─> Eureka responds: "http://localhost:8082"
   
4. REST Call Between Services
   └─> AdminService → HTTP GET → LoanMicroservice (8082)
   └─> Endpoint: GET /loan/admin/all
   └─> Response: List of all loans
   
5. Response to Client
   └─> AdminService assembles data from multiple sources
   └─> Returns to frontend:
      ```json
      {
        admin: { id, name, email, role },
        loans: [ { id, amount, status, user }, ... ]
      }
      ```
```

---

## 🔐 Security & JWT Flow

### JWT Token Flow

```
┌─────────────────────────────────────────────────────────────┐
│ 1. LOGIN REQUEST                                            │
│ POST /admin-login                                           │
│ Body: { email: "admin@test.com", password: "pass123" }    │
└─────────────────┬───────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────────────────────────┐
│ 2. CREDENTIAL VERIFICATION                                  │
│ - Query admin_users table                                   │
│ - Find user by email                                        │
│ - Verify password with BCryptPasswordEncoder               │
└─────────────────┬───────────────────────────────────────────┘
                  ↓ (if verified)
┌─────────────────────────────────────────────────────────────┐
│ 3. JWT GENERATION                                           │
│ Secret Key: 404E635266556A586E3272357538782F413F4428472B... │
│ Header: { alg: "HS256", typ: "JWT" }                       │
│ Payload: {                                                  │
│   sub: "admin123",                                          │
│   email: "admin@test.com",                                  │
│   role: "ADMIN",                                            │
│   iat: 1708534794,                                          │
│   exp: 1708535694                                           │
│ }                                                            │
│ Signature: HMAC-SHA256(header.payload, secret)             │
└─────────────────┬───────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────────────────────────┐
│ 4. TOKEN RESPONSE                                           │
│ HTTP 200 OK                                                 │
│ Body: {                                                     │
│   token: "eyJhbGciOiJIUzI1NiIsInR...",                     │
│   user: { id: 1, email: "admin@test.com", role: "ADMIN" } │
│ }                                                            │
└─────────────────┬───────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────────────────────────┐
│ 5. SUBSEQUENT REQUESTS                                      │
│ GET /admin-dashboard                                        │
│ Header: Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR...  │
└─────────────────┬───────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────────────────────────┐
│ 6. JWT VERIFICATION (JwtAuthenticationFilter)              │
│ - Extract token from Authorization header                   │
│ - Verify signature using secret key                         │
│ - Check expiration time                                     │
│ - Extract user info (sub, email, role)                      │
│ - Set SecurityContext with authenticated user               │
└─────────────────┬───────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────────────────────────┐
│ 7. REQUEST PROCESSING                                       │
│ - User is authenticated                                     │
│ - User has role ADMIN                                       │
│ - Access to /admin-dashboard is authorized                 │
│ - Endpoint handler executes                                 │
└─────────────────┬───────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────────────────────────┐
│ 8. RESPONSE                                                  │
│ HTTP 200 OK                                                 │
│ Body: { dashboardData: {...} }                             │
└─────────────────────────────────────────────────────────────┘
```

---

## 📊 Database Schema & Relationships

### admin_users Table
```sql
CREATE TABLE admin_users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,              -- BCrypt hashed
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    role ENUM('ADMIN', 'SUPER_ADMIN') NOT NULL DEFAULT 'ADMIN',
    status ENUM('ACTIVE', 'INACTIVE', 'LOCKED') NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### loans Table
```sql
CREATE TABLE loans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,                     -- Foreign key to user table
    amount DECIMAL(12, 2) NOT NULL,
    interest_rate DECIMAL(5, 2) NOT NULL,
    tenure INT NOT NULL,                         -- In months
    emi DECIMAL(12, 2),                          -- Calculated EMI
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'DISBURSED') DEFAULT 'PENDING',
    loan_type VARCHAR(50),                       -- Personal, Home, Auto, etc
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES admin_users(id)
);
```

---

## 🔌 Port Mappings & Service Endpoints

### Port Reference

| Service | Port | Protocol | Purpose | Status Check |
|---------|------|----------|---------|--------------|
| **MySQL** | 3306 | TCP | Database | `mysql -u root -p0205 -e "SELECT 1"` |
| **ServiceRegistry** | 8761 | HTTP | Eureka Server | `http://localhost:8761` |
| **AdminMicroservice** | 8081 | HTTP | Admin APIs | `http://localhost:8081/admin-login` |
| **ApiGateway** | 8080 | HTTP | API Routing | `http://localhost:8080` |
| **LoanMicroservice** | 8082 | HTTP | Loan APIs | `http://localhost:8082` |

### Core Endpoints

#### AdminMicroservice (8081)
```
GET  /                              → Welcome page
GET  /admin-login                   → Login form
POST /admin/login                   → Login endpoint
GET  /admin-dashboard               → Dashboard (requires auth)
GET  /admin/profile                 → User profile
PUT  /admin/profile                 → Update profile
POST /admin/logout                  → Logout
GET  /index                         → Home page
```

#### API Gateway (8080)
```
GET  /customer-dashboard            → Customer view
GET  /admin-login                   → Redirect to admin service
POST /loan/apply                    → Apply for loan
GET  /loan/{id}                     → Get loan details
GET  /loans                         → List loans
PUT  /loan/{id}                     → Update loan
DELETE /loan/{id}                   → Delete loan
```

#### ServiceRegistry (8761)
```
GET  /                              → Eureka home page
GET  /eureka/apps                   → All registered applications (XML)
GET  /eureka/apps/json              → All registered applications (JSON)
GET  /eureka/apps/{appName}         → Specific application info
```

---

## 🚀 Data Flow Example: Complete Loan Application

```
1. Frontend → Browser
   └─> User fills loan application form
       - Amount: ₹500,000
       - Tenure: 12 months
       - Type: Personal Loan

2. Browser → API Gateway (http://localhost:8080/loan/apply)
   └─> POST /loan/apply
   └─> Content-Type: application/json
   └─> Body: { amount: 500000, tenure: 12, loanType: "PERSONAL" }
   └─> Headers: { Authorization: "Bearer [JWT_TOKEN]" }

3. API Gateway → LoanMicroservice (on port 8082)
   └─> Routes request to LoanMicroservice based on path
   └─> Adds tracing headers for debugging

4. LoanMicroservice → JwtAuthenticationFilter
   └─> Extracts JWT from Authorization header
   └─> Validates token signature and expiration
   └─> Sets SecurityContext with user: { id: 1, role: "CUSTOMER" }

5. LoanMicroservice → LoanController.applyForLoan()
   └─> Receives request body
   └─> Validates: amount ≥ 50000 AND amount ≤ 5000000
   └─> Validates: tenure > 0 AND tenure ≤ 60
   └─> Calculates EMI:
       EMI = P × r × (1+r)^n / ((1+r)^n - 1)
       where P = principal, r = monthly rate, n = months

6. LoanMicroservice → Database Layer (Hibernate)
   └─> Creates Loan entity object
   └─> Calls loanRepository.save(loan)
   └─> Hibernate translates to SQL:
       ```sql
       INSERT INTO loans
       (user_id, amount, interest_rate, tenure, emi, status, loan_type, created_at)
       VALUES (1, 500000, 7.5, 12, 42341.54, 'PENDING', 'PERSONAL', NOW());
       ```

7. HikariCP → MySQL Connection Pool
   └─> Gets available connection from pool (or creates new)
   └─> Establishes TCP connection to MySQL:3306
   └─> Sends SQL query to MySQL server

8. MySQL → Execution & Storage
   └─> Validates schema
   └─> Inserts row into loans table
   └─> Returns auto-generated loan_id = 456
   └─> Returns affected rows = 1

9. LoanMicroservice → Response Object
   └─> Hibernate returns persisted Loan entity with id=456
   └─> LoanController wraps in response DTO:
       ```json
       {
         "loanId": 456,
         "userId": 1,
         "amount": 500000,
         "interestRate": 7.5,
         "tenure": 12,
         "emi": 42341.54,
         "status": "PENDING",
         "loanType": "PERSONAL",
         "createdAt": "2026-02-21T23:40:00Z"
       }
       ```

10. API Gateway → Response Forwarding
    └─> Receives response from LoanMicroservice
    └─> Adds CORS headers if needed
    └─> Forwards to client

11. Browser → Frontend Receives Response
    └─> HTTP 201 CREATED
    └─> Body: { loanId: 456, status: "PENDING", ... }
    └─> JavaScript processes response
    └─> Displays confirmation message:
        "✓ Loan application submitted!
         Loan ID: 456
         Status: PENDING
         EMI: ₹42,341.54"

12. Frontend → User Experience Update
    └─> Show loan details page
    └─> Display loan reference number
    └─> Provide option to check status later
    └─> Save loan ID to localStorage for tracking
```

---

## 📈 Microservices Communication Pattern

### Synchronous Communication (REST API)

```
AdminMicroservice
        │
        │ Need to get all loans?
        ↓
    1. Query ServiceRegistry (Eureka)
        │ "Where is LoanMicroservice?"
        ↓
    2. Eureka responds
        │ "LoanMicroservice at http://localhost:8082"
        ↓
    3. HTTP GET → http://localhost:8082/loan/all-admin
        │
        ↓ (wait for response)
        │
    4. LoanMicroservice responds with loan list
        │
        ↓
    5. AdminService receives and processes
        │
        ↓
    6. Response sent to client
```

---

## ⚙️ Configuration Hierarchy

```
Default Values (hardcoded)
        ↓ overridden by
application.yml (in JAR resources)
        ↓ overridden by
Environment Variables
        ↓ overridden by
JVM System Properties
        ↓ overridden by
Command-line Arguments

Example - Database credentials:
1. Default: datasource.username="sa"
2. application.yml: ${SPRING_DATASOURCE_USERNAME:root}
3. Environment Variable: $env:SPRING_DATASOURCE_USERNAME="admin"
   ← WINS (lowest priority)
```

---

## 🔄 Startup Initialization Order

```
Java Process Starts
        ↓
Load application.yml
        ↓
Configure Spring environment
        ↓
Scan classpath for components
        ↓
Create bean instances (in order of dependencies)
        ├─ DataSource (MySQL connection details)
        ├─ EntityManagerFactory (Hibernate)
        ├─ Repository beans
        ├─ Service beans
        └─ Controller beans
        ↓
HikariCP initializes connection pool
        ↓
Hibernate creates tables (ddl-auto: update)
        ↓
Register routes in DispatcherServlet
        ↓
Register with ServiceRegistry/Eureka
        ↓
Start Tomcat embedded server
        ↓
Application Ready!
        └─> Listening on port 8081
```

---

## 📊 Load Testing Scenario

```
1000 concurrent customers apply for loans:

1. All requests → API Gateway (8080)
2. Gateway distributes to:
   - LoanMicroservice instance 1
   - LoanMicroservice instance 2
   - LoanMicroservice instance 3 (if deployed)
3. Each instance queries ServiceRegistry
4. Eureka provides list of available instances
5. Services use HikariCP connection pool
   - Max pool size: 10 connections (default)
   - Each connection handles 1+ request
6. MySQL handles requests in queue
7. Response time: ~200ms per request average
```

---

## 🎯 Quick Reference - What Runs Where

| Component | Runs On | Port | Technology |
|-----------|---------|------|------------|
| Browser | Your Computer | - | HTML/CSS/JavaScript |
| API Gateway | JVM Process 1 | 8080 | Spring Cloud Gateway |
| AdminMicroservice | JVM Process 2 | 8081 | Spring Boot + Hibernate |
| LoanMicroservice | JVM Process 3 | 8082 | Spring Boot + Hibernate |
| ServiceRegistry | JVM Process 4 | 8761 | Netflix Eureka |
| MySQL | JVM or System Service | 3306 | MySQL 8.x RDBMS |
| Maven | Build Tool | N/A | Build Automation |

---

**For startup commands, see: QUICK_COMMANDS.md**
**For troubleshooting, see: STARTUP_GUIDE.md**
