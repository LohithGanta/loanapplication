# QUICK START COMMANDS - Loan Application Microservices

## 🚀 FASTEST WAY TO START EVERYTHING

```powershell
# Batch file (Windows batch)
& "C:\Users\lohith\Desktop\Loan_application\demo\start-all-services.bat"

# PowerShell script
C:\Users\lohith\Desktop\Loan_application\demo\start-all-services.ps1
```

---

## ⚡ QUICK COMMANDS BY SERVICE

### 1. ServiceRegistry / Eureka (Port 8761)

**Batch File:**
```batch
& "C:\Users\lohith\Desktop\Loan_application\demo\start-service-registry.bat"
```

**Terminal:**
```powershell
cd C:\Users\lohith\Desktop\Loan_application\demo
.\mvnw.cmd spring-boot:run -pl ServiceRegistry
```

**Check Status:**
```powershell
netstat -ano | findstr :8761
# Access: http://localhost:8761
```

---

### 2. AdminMicroservice (Port 8081)

**Batch File:**
```batch
& "C:\Users\lohith\Desktop\Loan_application\demo\start-admin-microservice.bat"
```

**Terminal:**
```powershell
cd C:\Users\lohith\Desktop\Loan_application\demo
$env:SPRING_DATASOURCE_USERNAME='root'
$env:SPRING_DATASOURCE_PASSWORD='0205'
.\mvnw.cmd spring-boot:run -pl AdminMicroservice
```

**Check Status:**
```powershell
netstat -ano | findstr :8081
# Access: http://localhost:8081/admin-login
```

---

### 3. API Gateway (Port 8080)

**Batch File:**
```batch
& "C:\Users\lohith\Desktop\Loan_application\demo\start-api-gateway.bat"
```

**Terminal:**
```powershell
cd C:\Users\lohith\Desktop\Loan_application\demo
.\mvnw.cmd spring-boot:run -pl ApiGateway
```

**Check Status:**
```powershell
netstat -ano | findstr :8080
# Access: http://localhost:8080
```

---

### 4. LoanMicroservice

**Terminal:**
```powershell
cd C:\Users\lohith\Desktop\Loan_application\demo
.\mvnw.cmd spring-boot:run -pl LoanMicroservice
```

---

## 🔨 BUILD & COMPILE COMMANDS

### Clean Build All Modules
```powershell
cd C:\Users\lohith\Desktop\Loan_application\demo
.\mvnw.cmd clean install -DskipTests
```

### Build Specific Module
```powershell
# AdminMicroservice only
.\mvnw.cmd clean install -DskipTests -pl AdminMicroservice

# ServiceRegistry only
.\mvnw.cmd clean install -DskipTests -pl ServiceRegistry

# Multiple modules
.\mvnw.cmd clean install -DskipTests -pl ServiceRegistry,AdminMicroservice
```

### Compile Without Tests
```powershell
.\mvnw.cmd compile -DskipTests
```

### Run Tests
```powershell
.\mvnw.cmd test
```

---

## 🔍 VERIFICATION & MONITORING COMMANDS

### Check All Services Running
```powershell
# Check all ports
netstat -ano | findstr :876
netstat -ano | findstr :808
netstat -ano | findstr :330

# Individual checks
netstat -ano | findstr :8761    # ServiceRegistry
netstat -ano | findstr :8081    # AdminMicroservice
netstat -ano | findstr :8080    # API Gateway
netstat -ano | findstr :3306    # MySQL
```

### Find What's Using a Port
```powershell
# Find process using port 8081
netstat -ano | findstr :8081
# Returns: TCP 0.0.0.0:8081 0.0.0.0:0 LISTENING [PID]

# Get process details
Get-Process | Where-Object { $_.Id -eq [PID] }
```

### Kill Process Using Port
```powershell
# Kill process using port 8081
taskkill /PID [PID] /F

# Or kill all Java processes
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
```

---

## 📊 ACCESS URLS

| Service | URL | Purpose |
|---------|-----|---------|
| **ServiceRegistry** | http://localhost:8761 | Eureka server |
| **Admin Login** | http://localhost:8081/admin-login | Admin dashboard login |
| **Admin Dashboard** | http://localhost:8081/admin-dashboard | Admin dashboard (after login) |
| **Customer Dashboard** | http://localhost:8080/customer-dashboard | Customer interface |
| **API Gateway** | http://localhost:8080 | Main API entry point |

---

## 🗄️ DATABASE COMMANDS

### Connect to MySQL
```bash
mysql -u root -p0205 admin_db

# Once connected, useful queries:
SHOW TABLES;
SELECT * FROM admin_users;
DESCRIBE admin_users;
```

### Check MySQL Service
```powershell
# Check if MySQL is running
sc query mysql80

# Start MySQL
net start MySQL80

# Stop MySQL
net stop MySQL80

# Verify port 3306
netstat -ano | findstr :3306
```

### Reset Database
```sql
-- Connect to MySQL and run:
DROP DATABASE IF EXISTS admin_db;
CREATE DATABASE admin_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE admin_db;

-- Tables will be auto-created by Hibernate on startup
```

---

## 🛑 STOPPING SERVICES

### Stop Individual Service
1. Click the console window
2. Press `Ctrl+C`
3. Type `Y` when asked to terminate batch

### Stop All Services at Once
```powershell
# Option 1: Kill all Java processes
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force

# Option 2: Use Task Manager
# - Press Ctrl+Shift+Esc
# - Find java.exe
# - Right-click → End Task

# Option 3: Use taskkill
taskkill /IM java.exe /F
```

---

## 🔧 CONFIGURATION CHANGES

### Change MySQL Credentials

**Edit application.yml:**
```yaml
# File: AdminMicroservice/src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/admin_db
    username: ${SPRING_DATASOURCE_USERNAME:root}         # Defaults to 'root'
    password: ${SPRING_DATASOURCE_PASSWORD:0205}         # Defaults to '0205'
```

**Or set environment variables:**
```powershell
$env:SPRING_DATASOURCE_USERNAME='myuser'
$env:SPRING_DATASOURCE_PASSWORD='mypassword'
.\mvnw.cmd spring-boot:run -pl AdminMicroservice
```

### Change Service Ports

Edit `application.yml` for each service:
```yaml
server:
  port: 8081          # Change this number
```

---

## 🐛 TROUBLESHOOTING QUICK FIXES

### "Port already in use"
```powershell
# Find what's using it
netstat -ano | findstr :[PORT]

# Kill the process
taskkill /PID [PID] /F
```

### "MySQL Connection refused"
```powershell
# Start MySQL service
net start MySQL80

# Test connection
mysql -u root -p0205 -e "SELECT 1 as alive;"
```

### "Cannot find mvnw.cmd"
```powershell
# Make sure you're in the right directory
cd C:\Users\lohith\Desktop\Loan_application\demo
dir mvnw.cmd        # Should show the file
```

### "Build failed: [error messages]"
```powershell
# Full rebuild
.\mvnw.cmd clean install -DskipTests

# Or skip tests and just compile
.\mvnw.cmd clean compile -DskipTests
```

### "No CompilationUnit for class"
```powershell
# Clean and rebuild
.\mvnw.cmd clean install -DskipTests
```

---

## 📋 SEQUENCE FOR FIRST-TIME STARTUP

1. **Verify MySQL is running:**
   ```powershell
   mysql -u root -p0205 -e "SELECT 'OK';"
   ```

2. **Build the project:**
   ```powershell
   cd C:\Users\lohith\Desktop\Loan_application\demo
   .\mvnw.cmd clean install -DskipTests
   ```

3. **Start ServiceRegistry:**
   ```powershell
   & ".\start-service-registry.bat"
   ```

4. **Wait 5 seconds, then start AdminMicroservice:**
   ```powershell
   & ".\start-admin-microservice.bat"
   ```

5. **Wait 5 seconds, then start API Gateway:**
   ```powershell
   & ".\start-api-gateway.bat"
   ```

6. **Verify all are running:**
   ```powershell
   netstat -ano | findstr :8761
   netstat -ano | findstr :8081
   netstat -ano | findstr :8080
   ```

7. **Open in browser:**
   - http://localhost:8761 (Eureka)
   - http://localhost:8081/admin-login (Admin)
   - http://localhost:8080 (Gateway)

---

## 💾 IMPORTANT FILE LOCATIONS

```
C:\Users\lohith\Desktop\Loan_application\demo\
├── start-all-services.bat           ← Run all services at once
├── start-all-services.ps1           ← PowerShell version
├── start-service-registry.bat       ← ServiceRegistry only
├── start-admin-microservice.bat     ← AdminMicroservice only
├── start-api-gateway.bat            ← API Gateway only
├── STARTUP_GUIDE.md                 ← Full documentation
├── QUICK_COMMANDS.txt              ← This file
├── pom.xml                          ← Project configuration
├── mvnw.cmd                         ← Maven wrapper (Windows)
│
├── ServiceRegistry/
│   └── src/main/resources/application.yml
│
├── AdminMicroservice/
│   └── src/main/resources/application.yml
│
├── ApiGateway/
│   └── src/main/resources/application.yml
│
├── LoanMicroservice/
│   └── src/main/resources/application.yml
│
└── src/main/resources/
    └── application.properties
```

---

## 🎯 ONE-LINE SUMMARY

**Everything at once:**
```powershell
& "C:\Users\lohith\Desktop\Loan_application\demo\start-all-services.bat"
```

**Then access:**
- Eureka: http://localhost:8761
- Admin: http://localhost:8081/admin-login  
- Gateway: http://localhost:8080

---

**Need help?** See `STARTUP_GUIDE.md` in the same directory.
