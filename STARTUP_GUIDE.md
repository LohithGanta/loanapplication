# Loan Application - Complete Startup Guide

## 🏗️ Architecture Overview

```
Frontend (HTML/JS) → API Gateway (8080) → AdminMicroservice (8081) + LoanMicroservice
                                      ↓
                           ServiceRegistry/Eureka (8761)
                                      ↓
                              MySQL Database (admin_db)
```

---

## 📋 Prerequisites

- **Java 17+** installed
- **MySQL 8.x** running with:
  - Database: `admin_db`
  - User: `root`
  - Password: `0205`
- **Maven 3.6+** (or use included `mvnw.cmd`)
- **Ports Available**: 8761, 8081, 8080

---

## 🚀 Startup Sequence

### **Step 1: Start MySQL Database**

Ensure MySQL is running:

```bash
# Windows - Check if MySQL is running
netstat -ano | findstr :3306

# If MySQL is not running, start it (Windows Services)
net start MySQL80
```

**Verify MySQL is accessible:**
```bash
mysql -u root -p0205 -e "SELECT 'MySQL is UP' as status;"
```

---

### **Step 2: Start ServiceRegistry (Eureka) - Port 8761**

**Option A: Using Batch File (Fastest)**
```bash
& "C:\Users\lohith\Desktop\Loan_application\demo\start-service-registry.bat"
```

**Option B: Using Terminal**
```powershell
cd C:\Users\lohith\Desktop\Loan_application\demo
.\mvnw.cmd spring-boot:run -pl ServiceRegistry
```

**Verify ServiceRegistry is running:**
```bash
# Check port 8761
netstat -ano | findstr :8761

# Access Eureka Dashboard
# http://localhost:8761
```

**Expected Output:**
```
Tomcat started on port 8761 (http)
Started ServiceRegistryApplication in X.X seconds
```

---

### **Step 3: Start AdminMicroservice - Port 8081**

**Option A: Using Batch File (Recommended)**
```bash
& "C:\Users\lohith\Desktop\Loan_application\demo\start-admin-microservice.bat"
```

**Option B: Using Terminal**
```powershell
cd C:\Users\lohith\Desktop\Loan_application\demo
$env:SPRING_DATASOURCE_USERNAME='root'
$env:SPRING_DATASOURCE_PASSWORD='0205'
.\mvnw.cmd spring-boot:run -pl AdminMicroservice
```

**Verify AdminMicroservice is running:**
```bash
# Check port 8081
netstat -ano | findstr :8081

# Access Admin Login Page
# http://localhost:8081/admin-login
```

**Expected Output:**
```
Tomcat started on port 8081 (http)
HikariPool-1 - Added connection com.mysql.cj.jdbc.ConnectionImpl@...
Registering application ADMINMICROSERVICE with eureka with status UP
```

---

### **Step 4: Start ApiGateway - Port 8080**

**Option A: Using Terminal**
```powershell
cd C:\Users\lohith\Desktop\Loan_application\demo
.\mvnw.cmd spring-boot:run -pl ApiGateway
```

**Option B: Using Batch File (Create if needed)**
```batch
@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Starting API Gateway on port 8080
echo ========================================

echo Prerequisites:
echo  - ServiceRegistry must be running on port 8761
echo  - AdminMicroservice must be running on port 8081

timeout /t 3
cd /d "%~dp0"

echo Starting application...
call mvnw.cmd spring-boot:run -pl ApiGateway

echo API Gateway stopped.
pause
endlocal
```

**Verify ApiGateway is running:**
```bash
netstat -ano | findstr :8080
```

---

### **Step 5: Start LoanMicroservice**

**Option A: Using Terminal**
```powershell
cd C:\Users\lohith\Desktop\Loan_application\demo
.\mvnw.cmd spring-boot:run -pl LoanMicroservice
```

**Verify LoanMicroservice is running:**
```bash
# Check the configured port (from application.yml)
# Usually on 8082 or similar
netstat -ano | findstr :8082
```

---

## 🔄 Complete Automated Startup (All Services)

### **Master Startup Script**

Create file: `C:\Users\lohith\Desktop\Loan_application\demo\start-all-services.bat`

```batch
@echo off
setlocal enabledelayedexpansion

color 0A
cls

echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║        LOAN APPLICATION - COMPLETE MICROSERVICES STARTUP       ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.

echo [Step 1 of 5] Verifying prerequisites...
echo ─────────────────────────────────────────────────────────────────
echo ✓ Checking MySQL connectivity...
mysql -u root -p0205 -e "SELECT 'MySQL OK' as status;" >nul 2>&1
if errorlevel 1 (
    echo ✗ ERROR: MySQL is not accessible (root/0205)
    echo   Please start MySQL and try again.
    pause
    exit /b 1
)
echo ✓ MySQL is running on port 3306

echo.
echo [Step 2 of 5] Building all modules...
echo ─────────────────────────────────────────────────────────────────
cd /d "%~dp0"
call mvnw.cmd clean install -DskipTests
if errorlevel 1 (
    echo ✗ Build failed. See errors above.
    pause
    exit /b 1
)
echo ✓ Build completed successfully

echo.
echo [Step 3 of 5] Starting ServiceRegistry (Eureka) on port 8761...
echo ─────────────────────────────────────────────────────────────────
start "ServiceRegistry" cmd /k "mvnw.cmd spring-boot:run -pl ServiceRegistry"
timeout /t 5

echo.
echo [Step 4 of 5] Starting AdminMicroservice on port 8081...
echo ─────────────────────────────────────────────────────────────────
set SPRING_DATASOURCE_USERNAME=root
set SPRING_DATASOURCE_PASSWORD=0205
start "AdminMicroservice" cmd /k "mvnw.cmd spring-boot:run -pl AdminMicroservice"
timeout /t 5

echo.
echo [Step 5 of 5] Starting API Gateway on port 8080...
echo ─────────────────────────────────────────────────────────────────
start "ApiGateway" cmd /k "mvnw.cmd spring-boot:run -pl ApiGateway"
timeout /t 3

echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║                   ALL SERVICES STARTED                          ║
echo ╠════════════════════════════════════════════════════════════════╣
echo ║ Service              Port    URL                               ║
echo ├────────────────────────────────────────────────────────────────┤
echo ║ ServiceRegistry      8761    http://localhost:8761             ║
echo ║ AdminMicroservice    8081    http://localhost:8081             ║
echo ║ API Gateway          8080    http://localhost:8080             ║
echo ║ MySQL                3306    mysql://root@localhost:3306       ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
echo Opening browser windows...
timeout /t 2

start http://localhost:8761
timeout /t 1
start http://localhost:8081/admin-login
timeout /t 1
start http://localhost:8080

echo.
echo ✓ All services started. Check the console windows for activity.
echo ✓ Press Ctrl+C in any window to stop that service.
echo.
pause
endlocal
```

**Run Master Startup:**
```powershell
& "C:\Users\lohith\Desktop\Loan_application\demo\start-all-services.bat"
```

---

## ✅ Verification Commands

### **Check All Services Running**

```powershell
# Check ServiceRegistry (8761)
netstat -ano | findstr :8761

# Check AdminMicroservice (8081)
netstat -ano | findstr :8081

# Check API Gateway (8080)
netstat -ano | findstr :8080

# Check MySQL (3306)
netstat -ano | findstr :3306
```

### **Expected Output**
```
TCP    0.0.0.0:8761           0.0.0.0:0              LISTENING       [PID]
TCP    0.0.0.0:8081           0.0.0.0:0              LISTENING       [PID]
TCP    0.0.0.0:8080           0.0.0.0:0              LISTENING       [PID]
TCP    0.0.0.0:3306           0.0.0.0:0              LISTENING       [PID]
```

---

## 🌐 Access the Application

| Component | URL | Purpose |
|-----------|-----|---------|
| **ServiceRegistry** | http://localhost:8761 | View registered services |
| **Admin Login** | http://localhost:8081/admin-login | Admin dashboard |
| **API Gateway** | http://localhost:8080 | Route requests to microservices |
| **Customer Dashboard** | http://localhost:8080/customer-dashboard | Customer interface |

---

## 📊 Frontend-to-Backend Request Flow

```
┌─────────────────────┐
│   Browser/Frontend  │
│  (HTML/JavaScript)  │
└──────────┬──────────┘
           │
           ↓
┌─────────────────────────────────┐
│   API Gateway (8080)             │
│  - Request routing               │
│  - Load balancing                │
│  - Security headers              │
└──────────┬──────────────────────┘
           │
     ┌─────┴─────┐
     ↓           ↓
┌─────────┐  ┌─────────────────┐
│ Admin   │  │ Loan            │
│ Service │  │ Microservice    │
│ (8081)  │  │ (8082)          │
└────┬────┘  └─────────┬───────┘
     │                 │
     └─────────┬───────┘
               ↓
     ┌─────────────────────┐
     │  ServiceRegistry    │
     │  (Eureka - 8761)    │
     │ - Service Discovery │
     │ - Health Checks     │
     └──────────┬──────────┘
                │
                ↓
     ┌─────────────────────┐
     │  MySQL Database     │
     │  (admin_db)         │
     │  Port 3306          │
     └─────────────────────┘
```

---

## 🛑 Stopping Services

### **Individual Service Shutdown**
1. Click the console window of the service
2. Press `Ctrl+C`
3. Type `Y` and press `Enter`

### **Complete Shutdown**
```powershell
# Kill all Java processes running Maven
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force

# Or manually stop each:
# - Close each service console window
# - Or run in Task Manager: Kill java.exe processes
```

---

## 🔧 Configuration Files

| File | Path | Purpose |
|------|------|---------|
| **application.yml** | `AdminMicroservice/src/main/resources/` | MySQL credentials, Eureka config |
| **pom.xml** | Root directory | Maven dependencies, version management |
| **start-service-registry.bat** | Root directory | ServiceRegistry startup script |
| **start-admin-microservice.bat** | Root directory | AdminMicroservice startup script |

### **Edit Credentials**

If you need to change MySQL credentials, edit:

```yaml
# File: AdminMicroservice/src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/admin_db
    username: ${SPRING_DATASOURCE_USERNAME:root}  # Change default here
    password: ${SPRING_DATASOURCE_PASSWORD:0205}  # Change default here
```

Or set environment variables:
```powershell
$env:SPRING_DATASOURCE_USERNAME='newuser'
$env:SPRING_DATASOURCE_PASSWORD='newpassword'
```

---

## 🐛 Troubleshooting

### **Port Already in Use**
```powershell
# Find what's using the port
netstat -ano | findstr :8081

# Kill the process (replace PID)
taskkill /PID [PID] /F
```

### **MySQL Connection Refused**
```bash
# Verify MySQL credentials
mysql -u root -p0205

# Or check MySQL service
sc query mysql80
net start mysql80
```

### **Eureka Registration Failed**
```
# Wait 30 seconds and check again
# ServiceRegistry must be running BEFORE AdminMicroservice
# Check logs for error messages
```

### **Build Fails**
```powershell
# Clean build
cd C:\Users\lohith\Desktop\Loan_application\demo
.\mvnw.cmd clean install -DskipTests

# If still fails, check Java version
java -version
```

---

## 📝 Quick Reference Commands

```powershell
# Full build
cd C:\Users\lohith\Desktop\Loan_application\demo
.\mvnw.cmd clean install -DskipTests

# Start specific service
.\mvnw.cmd spring-boot:run -pl ServiceRegistry          # Port 8761
.\mvnw.cmd spring-boot:run -pl AdminMicroservice        # Port 8081
.\mvnw.cmd spring-boot:run -pl ApiGateway               # Port 8080

# Skip tests and build faster
.\mvnw.cmd clean install -DskipTests

# View logs from a running service
# (Check the console window where it was started)

# Check running services
netstat -ano | findstr :876
netstat -ano | findstr :808
netstat -ano | findstr :330
```

---

## 🎯 Summary

**Expected startup time:** ~30-40 seconds total

**Services in execution order:**
1. MySQL (system service - must be pre-running)
2. ServiceRegistry / Eureka (8761)
3. AdminMicroservice (8081)
4. ApiGateway (8080)
5. LoanMicroservice (8082)

**Frontend flow:**
- User accesses http://localhost:8080 (API Gateway)
- Gateway routes to AdminMicroservice or LoanMicroservice
- Services register with Eureka for discovery
- All services connect to MySQL database

---

**Ready to start? Run this command:**
```powershell
& "C:\Users\lohith\Desktop\Loan_application\demo\start-all-services.bat"
```

