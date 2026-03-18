# 📋 Complete Command Reference & Cheat Sheet

## 🎯 The FASTEST Way - Pick One

### 1. Batch File (One Console, All Services)
```batch
& "C:\Users\lohith\Desktop\Loan_application\demo\start-all-services.bat"
```

### 2. PowerShell Script
```powershell
C:\Users\lohith\Desktop\Loan_application\demo\start-all-services.ps1
```

### 3. Three Separate Terminals (See Logs Clearly)
Terminal 1:
```powershell
cd C:\Users\lohith\Desktop\Loan_application\demo
.\mvnw.cmd spring-boot:run -pl ServiceRegistry
```

Terminal 2 (wait 5 seconds, then):
```powershell
cd C:\Users\lohith\Desktop\Loan_application\demo
$env:SPRING_DATASOURCE_USERNAME='root'
$env:SPRING_DATASOURCE_PASSWORD='0205'
.\mvnw.cmd spring-boot:run -pl AdminMicroservice
```

Terminal 3 (wait 5 seconds, then):
```powershell
cd C:\Users\lohith\Desktop\Loan_application\demo
.\mvnw.cmd spring-boot:run -pl ApiGateway
```

---

## 📊 Service Startup Commands Matrix

| Service | Port | Terminal Command | Batch File |
|---------|------|------------------|-----------|
| **ServiceRegistry/Eureka** | 8761 | `.\mvnw.cmd spring-boot:run -pl ServiceRegistry` | `start-service-registry.bat` |
| **AdminMicroservice** | 8081 | `$env:SD_USER='root'`<br/>`$env:SD_PASS='0205'`<br/>`.\mvnw.cmd spring-boot:run -pl AdminMicroservice` | `start-admin-microservice.bat` |
| **API Gateway** | 8080 | `.\mvnw.cmd spring-boot:run -pl ApiGateway` | `start-api-gateway.bat` |
| **LoanMicroservice** | 8082 | `.\mvnw.cmd spring-boot:run -pl LoanMicroservice` | (create batch file) |

**Note:** `SD_USER` and `SD_PASS` are short for `SPRING_DATASOURCE_USERNAME` and `SPRING_DATASOURCE_PASSWORD`

---

## 🏗️ Build Commands

```powershell
cd C:\Users\lohith\Desktop\Loan_application\demo

# Clean build all modules (first time)
.\mvnw.cmd clean install -DskipTests

# Build specific module
.\mvnw.cmd clean install -DskipTests -pl AdminMicroservice

# Quick compile without packaging
.\mvnw.cmd compile -DskipTests

# Build with test execution
.\mvnw.cmd clean install

# Build only ServiceRegistry and AdminMicroservice
.\mvnw.cmd clean install -DskipTests -pl ServiceRegistry,AdminMicroservice
```

---

## 🔍 Verification Commands Table

### Check Services Running

```powershell
# All important ports
$ports = 8761, 8081, 8080, 3306
$ports | ForEach-Object { 
    Write-Host "Port $_:" -ForegroundColor Yellow
    netstat -ano | findstr ":$_"
}
```

Or individually:

| Port | Service | Command |
|------|---------|---------|
| 8761 | ServiceRegistry | `netstat -ano \| findstr :8761` |
| 8081 | AdminMicroservice | `netstat -ano \| findstr :8081` |
| 8080 | API Gateway | `netstat -ano \| findstr :8080` |
| 3306 | MySQL | `netstat -ano \| findstr :3306` |

**Expected Output:** `TCP 0.0.0.0:[PORT] 0.0.0.0:0 LISTENING [PID]`

### Check MySQL

```powershell
# Test connection
mysql -u root -p0205 -e "SELECT DATABASE();"

# List databases
mysql -u root -p0205 -e "SHOW DATABASES;"

# Check admin_db tables
mysql -u root -p0205 admin_db -e "SHOW TABLES;"

# View admin_users
mysql -u root -p0205 admin_db -e "SELECT * FROM admin_users;"

# Check MySQL service status
sc query mysql80

# Start/Stop MySQL
net start MySQL80
net stop MySQL80
```

---

## 🌐 URL Reference

| URL | Service | Purpose |
|-----|---------|---------|
| http://localhost:8761 | ServiceRegistry | Eureka dashboard, view registered services |
| http://localhost:8761/eureka/web | ServiceRegistry | Eureka UI (alternate) |
| http://localhost:8081 | AdminMicroservice | Service home page |
| http://localhost:8081/admin-login | AdminMicroservice | Admin login page |
| http://localhost:8081/admin-dashboard | AdminMicroservice | Admin dashboard (after login) |
| http://localhost:8080 | API Gateway | Gateway home page |
| http://localhost:8080/customer-dashboard | API Gateway | Customer interface |

---

## 🛑 Stop Commands

### Stop Individual Service
1. Click the terminal window
2. Press `Ctrl+C`
3. Type `Y` if prompted, then `Enter`

### Stop Specific Process

```powershell
# Find process using port 8081
$pid = (netstat -ano | findstr :8081).Split()[-1]

# Kill it
taskkill /PID $pid /F
```

### Stop ALL Java Services

```powershell
# PowerShell
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force

# Command Prompt
taskkill /IM java.exe /F
```

### Stop MySQL

```powershell
net stop MySQL80
```

---

## 🔧 Configuration Commands

### View Current Configuration

```powershell
# View java version
java -version

# View maven version
.\mvnw.cmd -version

# Check MySQL version
mysql -V
```

### Change Database Credentials

Edit file: `AdminMicroservice/src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/admin_db
    username: ${SPRING_DATASOURCE_USERNAME:newuser}     # Change here
    password: ${SPRING_DATASOURCE_PASSWORD:newpass}     # Change here
```

Or set environment variables:

```powershell
$env:SPRING_DATASOURCE_USERNAME='customuser'
$env:SPRING_DATASOURCE_PASSWORD='custompass'
.\mvnw.cmd spring-boot:run -pl AdminMicroservice
```

### Change Service Port

Edit `application.yml` for each service:

```yaml
server:
  port: 8081        # Change to any available port

eureka:
  instance:
    lease-renewal-interval-in-seconds: 10
    lease-expiration-duration-in-seconds: 30
```

---

## 📊 Process Management

### View All Java Processes

```powershell
# PowerShell
Get-Process java | Format-Table ProcessName, Id, Handles

# Command Prompt
tasklist | findstr java
```

### Get Details of Process on Port

```powershell
# PowerShell function
function Get-ProcessByPort {
    param($Port)
    $line = netstat -ano | findstr ":$Port"
    if ($line) {
        $pid = $line.Split()[-1]
        Get-Process | Where-Object {$_.Id -eq $pid}
    }
}

# Usage
Get-ProcessByPort 8081
```

### Check Process Memory Usage

```powershell
Get-Process java | Select-Object Name, Id, WorkingSet, Handles
```

---

## 🐛 Troubleshooting Command Collection

### Port Already in Use - Full Resolution

```powershell
# 1. Find what's using port 8081
$line = netstat -ano | findstr :8081
Write-Host "Found: $line"

# 2. Extract PID
$pid = $line.Split()[-1]
Write-Host "Process ID: $pid"

# 3. Get process details
Get-Process -Id $pid

# 4. Kill the process
taskkill /PID $pid /F
Write-Host "Process killed"

# 5. Verify port is free
netstat -ano | findstr :8081  # Should return nothing
```

### MySQL Connection Failed

```powershell
# 1. Check if MySQL is running
netstat -ano | findstr :3306

# 2. If not found, start MySQL
net start MySQL80

# 3. Verify connection
mysql -u root -p0205 -e "SELECT 1 as success;"

# 4. Check databases
mysql -u root -p0205 -e "SHOW DATABASES;" | findstr admin_db

# 5. If admin_db not found, create it
mysql -u root -p0205 -e "CREATE DATABASE admin_db;"
```

### Build Failed - Clean Rebuild

```powershell
cd C:\Users\lohith\Desktop\Loan_application\demo

# 1. Clean old build
.\mvnw.cmd clean

# 2. Delete target directories completely
Remove-Item -Recurse -Force .\*/target -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force .\AdminMicroservice\target -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force .\ServiceRegistry\target -ErrorAction SilentlyContinue

# 3. Rebuild from scratch
.\mvnw.cmd clean install -DskipTests -U

# 4. Check for errors
# (scroll up in terminal to see compilation errors)
```

### Cannot Find Main Class

```powershell
# 1. Verify you're in correct directory
cd C:\Users\lohith\Desktop\Loan_application\demo
dir AdminMicroservice\src\main\java\com\example\demo\*.java

# 2. Check pom.xml for main class
Select-String "mainClass\|start-class" AdminMicroservice\pom.xml

# 3. Rebuild
.\mvnw.cmd clean package -DskipTests

# 4. Check JAR was created
dir AdminMicroservice\target\*.jar
```

---

## 📈 Performance Monitoring

### Monitor Application Startup

```powershell
# Capture startup time
Measure-Command {
    & ".\mvnw.cmd" spring-boot:run -pl AdminMicroservice | Select-String "Tomcat started"
}
```

### Check Application Logs

```powershell
# Save logs to file
.\mvnw.cmd spring-boot:run -pl AdminMicroservice | Tee-Object "app.log"

# View logs later
Get-Content app.log | Select-String "ERROR\|Warn\|Exception"
```

### Monitor Database Connections

```bash
# While application is running
mysql -u root -p0205 -e "SHOW PROCESSLIST;"

# Check table sizes
mysql -u root -p0205 admin_db -e "SELECT table_name, table_rows FROM information_schema.tables WHERE table_schema='admin_db';"
```

---

## 🔄 Common Workflows

### Daily Startup Workflow

```powershell
# Step 1: Verify prerequisites (30 seconds)
Write-Host "Checking MySQL..." -ForegroundColor Yellow
mysql -u root -p0205 -e "SELECT 1" > $null
if ($?) { Write-Host "✓ MySQL OK" -ForegroundColor Green }
else { Write-Host "✗ MySQL Failed" -ForegroundColor Red; exit }

# Step 2: Navigate to project
cd C:\Users\lohith\Desktop\Loan_application\demo

# Step 3: Start all services
Write-Host "Starting all services..." -ForegroundColor Yellow
& ".\start-all-services.bat"

# Step 4: Wait and verify
Start-Sleep -Seconds 10
Write-Host "Verifying services..." -ForegroundColor Yellow
netstat -ano | findstr ":876[1]|:808[10]"

# Step 5: Open browser
Write-Host "Opening Eureka dashboard..." -ForegroundColor Yellow
Start-Process "http://localhost:8761"
```

### Development & Testing Workflow

```powershell
# 1. Edit code in IDE
# (e.g., modify AdminController.java)

# 2. Rebuild module
cd C:\Users\lohith\Desktop\Loan_application\demo
.\mvnw.cmd clean package -DskipTests -pl AdminMicroservice

# 3. Restart service
# Press Ctrl+C in AdminMicroservice terminal
# Run again: .\mvnw.cmd spring-boot:run -pl AdminMicroservice

# 4. Test changes
Invoke-WebRequest "http://localhost:8081/admin-login" | Select-Object StatusCode
```

### Database Reset Workflow

```powershell
# 1. Stop all services
# Ctrl+C in all terminals

# 2. Connect to MySQL
mysql -u root -p0205

# 3. Drop and recreate database (in MySQL prompt)
```

```sql
DROP DATABASE IF EXISTS admin_db;
CREATE DATABASE admin_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

```powershell
# 4. Start services again
# They will automatically recreate tables
& ".\start-admin-microservice.bat"
```

---

## 💾 File Locations Quick Reference

```
Main Project Directory:
C:\Users\lohith\Desktop\Loan_application\demo\

Key Files:
├── pom.xml                                    (Maven config)
├── mvnw.cmd                                   (Maven wrapper)
├── start-all-services.bat                     (MASTER STARTUP)
├── start-service-registry.bat
├── start-admin-microservice.bat
├── start-api-gateway.bat
└── Documentation:
    ├── README.md                              (START HERE)
    ├── QUICK_COMMANDS.md                      (This file)
    ├── STARTUP_GUIDE.md
    └── ARCHITECTURE.md

AdminMicroservice Configuration:
└── AdminMicroservice/src/main/resources/application.yml

Static Files:
└── src/main/resources/
    ├── static/
    │   ├── admin-login.css
    │   ├── admin-login.js
    └── templates/
        ├── admin-login.html
        └── admin-dashboard.html
```

---

## ⚡ Quick Macro/Script

Save this as `startup.ps1` for one-command startup:

```powershell
# startup.ps1 - One-command project startup

param(
    [switch]$Clean,
    [switch]$BuildOnly
)

$projectPath = "C:\Users\lohith\Desktop\Loan_application\demo"
cd $projectPath

if ($Clean) {
    Write-Host "Cleaning previous build..." -ForegroundColor Yellow
    .\mvnw.cmd clean
}

Write-Host "Building project..." -ForegroundColor Yellow
.\mvnw.cmd clean install -DskipTests -q

if ($BuildOnly) {
    Write-Host "✓ Build complete. Exiting." -ForegroundColor Green
    exit
}

Write-Host "Starting services..." -ForegroundColor Yellow
& ".\start-all-services.bat"
```

**Usage:**
```powershell
# Full startup with clean build
.\startup.ps1 -Clean

# Just build
.\startup.ps1 -BuildOnly

# Regular startup (uses cached build)
.\startup.ps1
```

---

## 📚 Reference Table: All Commands at a Glance

| What | Command | Notes |
|------|---------|-------|
| **Start Everything** | `& ".\start-all-services.bat"` | Fastest way |
| **Start ServiceRegistry** | `.\mvnw.cmd spring-boot:run -pl ServiceRegistry` | Port 8761 |
| **Start Admin Service** | `$env:SPRING_DATASOURCE_USERNAME='root'; $env:SPRING_DATASOURCE_PASSWORD='0205'; .\mvnw.cmd spring-boot:run -pl AdminMicroservice` | Port 8081 |
| **Start API Gateway** | `.\mvnw.cmd spring-boot:run -pl ApiGateway` | Port 8080 |
| **Build Clean** | `.\mvnw.cmd clean install -DskipTests` | Fresh build |
| **Build Specific** | `.\mvnw.cmd -pl AdminMicroservice clean install -DskipTests` | Module only |
| **Check Port** | `netstat -ano \| findstr :[PORT]` | Replace [PORT] |
| **Kill Service** | `taskkill /PID [PID] /F` | Replace [PID] |
| **Kill All Java** | `Get-Process java \| Stop-Process -Force` | Stop everything |
| **Check MySQL** | `mysql -u root -p0205 -e "SELECT 1;"` | Connection test |
| **Eureka URL** | `http://localhost:8761` | Browser |
| **Admin URL** | `http://localhost:8081/admin-login` | Browser |
| **Gateway URL** | `http://localhost:8080` | Browser |

---

## ✅ Verification Checklist

- [ ] MySQL running: `mysql -u root -p0205 -e "SELECT 1;"`
- [ ] All ports available: `netstat -ano | findstr :876[1]`
- [ ] In correct directory: `cd C:\Users\lohith\Desktop\Loan_application\demo`
- [ ] Project built: `.\mvnw.cmd clean install -DskipTests`
- [ ] ServiceRegistry running: `http://localhost:8761` (shows Eureka page)
- [ ] AdminMicroservice running: `http://localhost:8081/admin-login` (shows login form)
- [ ] API Gateway running: `http://localhost:8080` (accessible)

---

**🚀 YOU'RE READY! Pick any startup command above and run it now.**
