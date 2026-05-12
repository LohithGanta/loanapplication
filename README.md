# 🏦 Loan Application - Microservices Documentation

Welcome to the Loan Application microservices project! This directory contains everything you need to run, understand, and troubleshoot the entire system.

---

## 📚 Documentation Files

### **Quick Start** (Read First!)
- **[QUICK_COMMANDS.md](QUICK_COMMANDS.md)** ⭐ START HERE
  - One-liners for every common task
  - Copy-paste ready commands
  - No explanation needed, just run it

### **Complete Guides**
- **[STARTUP_GUIDE.md](STARTUP_GUIDE.md)** 📘 Detailed Instructions
  - Step-by-step startup process
  - Verification commands
  - Troubleshooting section
  - Port mappings and access URLs

- **[ARCHITECTURE.md](ARCHITECTURE.md)** 🏛️ System Design
  - How requests flow from frontend to database
  - Service communication patterns
  - Data models and schema
  - JWT authentication flow
  - Request/response examples

---

## 🚀 Quick Start (30 seconds)

### Option 1: Batch File (Fastest)
```batch
& "C:\Users\lohith\Desktop\Loan_application\demo\start-all-services.bat"
```

### Option 2: PowerShell
```powershell
C:\Users\lohith\Desktop\Loan_application\demo\start-all-services.ps1
```

### Option 3: Manual Commands
```powershell
cd C:\Users\lohith\Desktop\Loan_application\demo

# Terminal 1: ServiceRegistry
.\mvnw.cmd spring-boot:run -pl ServiceRegistry

# Terminal 2: AdminMicroservice
$env:SPRING_DATASOURCE_USERNAME='root'
$env:SPRING_DATASOURCE_PASSWORD='0205'
.\mvnw.cmd spring-boot:run -pl AdminMicroservice

# Terminal 3: API Gateway
.\mvnw.cmd spring-boot:run -pl ApiGateway
```

---

## 🌐 Access the Application

After starting all services, open in your browser:

| Component | URL | Purpose |
|-----------|-----|---------|
| **Eureka Dashboard** | http://localhost:8761 | Service Registry |
| **Admin Login** | http://localhost:8081/admin-login | Admin interface |
| **API Gateway** | http://localhost:8080 | Main entry point |

---

## 📋 Startup Scripts Provided

| Script | Purpose | Command |
|--------|---------|---------|
| `start-all-services.bat` | Start everything at once | `& ".\start-all-services.bat"` |
| `start-all-services.ps1` | PowerShell version | `.\start-all-services.ps1` |
| `start-service-registry.bat` | Start Eureka only | `& ".\start-service-registry.bat"` |
| `start-admin-microservice.bat` | Start Admin service only | `& ".\start-admin-microservice.bat"` |
| `start-api-gateway.bat` | Start API Gateway only | `& ".\start-api-gateway.bat"` |

---

## 🏗️ Project Structure

```
demo/
├── 📄 README.md (this file)
├── 📖 STARTUP_GUIDE.md          (Detailed startup instructions)
├── 📖 QUICK_COMMANDS.md         (Command reference)
├── 📖 ARCHITECTURE.md           (System design & request flows)
│
├── 🔥 start-all-services.bat    (Start ALL services at once)
├── 🔥 start-all-services.ps1    (PowerShell version)
├── 🔥 start-service-registry.bat
├── 🔥 start-admin-microservice.bat
├── 🔥 start-api-gateway.bat
│
├── pom.xml                       (Maven parent POM)
├── mvnw.cmd                      (Maven wrapper for Windows)
│
├── ServiceRegistry/              (Eureka service registry - Port 8761)
│   ├── pom.xml
│   └── src/main/java/...
│
├── AdminMicroservice/            (Admin service - Port 8081)
│   ├── pom.xml
│   ├── src/main/java/...
│   └── src/main/resources/
│       └── application.yml
│
├── ApiGateway/                   (API Gateway - Port 8080)
│   ├── pom.xml
│   └── src/main/java/...
│
├── LoanMicroservice/             (Loan service - Port 8082)
│   ├── pom.xml
│   └── src/main/java/...
│
└── src/main/resources/           (Static files)
    ├── static/
    │   ├── admin-login.css
    │   └── admin-login.js
    └── templates/
        ├── admin-login.html
        ├── admin-dashboard.html
        └── customer-dashboard.html
```

---

## 📊 System Ports

| Port | Service | Status |
|------|---------|--------|
| **3306** | MySQL Database | ✅ Manual start required |
| **8761** | ServiceRegistry (Eureka) | Starts automatically |
| **8081** | AdminMicroservice | Starts automatically |
| **8080** | API Gateway | Starts automatically |
| **8082** | LoanMicroservice | Starts automatically |

---

## 🔐 Database Credentials

```
MySQL Connection
├─ Host: localhost
├─ Port: 3306
├─ Username: root
├─ Password: ****
└─ Database: admin_db
```

**Note:** Change these in `AdminMicroservice/src/main/resources/application.yml` if needed.

---

## 🛠️ Daily Workflow

### 1️⃣ First Time Setup
```powershell
# Build the project
cd C:\Users\lohith\Desktop\Loan_application\demo
.\mvnw.cmd clean install -DskipTests
```

### 2️⃣ Start All Services
```powershell
# Run the master startup script
& ".\start-all-services.bat"

# OR one-by-one in separate terminals
```

### 3️⃣ Verify Services Running
```powershell
# Check ports are listening
netstat -ano | findstr :8761
netstat -ano | findstr :8081
netstr -ano | findstr :8080
netstat -ano | findstr :3306
```

### 4️⃣ Access Application
- http://localhost:8761 (Eureka)
- http://localhost:8081/admin-login (Admin)
- http://localhost:8080 (Gateway)

### 5️⃣ Stop Services
```powershell
# Close each console window OR
Get-Process java | Stop-Process -Force
```

---

## 🐛 Common Issues & Solutions

### "Port already in use"
```powershell
# Find what's using the port
netstat -ano | findstr :[PORT]

# Kill it
taskkill /PID [PID] /F
```

### "MySQL connection refused"
```powershell
# Start MySQL service
net start MySQL80

# Test
mysql -u root -p0205 -e "SELECT 1"
```

### "Build failed"
```powershell
# Clean rebuild
.\mvnw.cmd clean install -DskipTests
```

### "Cannot find class"
```powershell
# Make sure you're in the right directory
cd C:\Users\lohith\Desktop\Loan_application\demo
dir mvnw.cmd  # Should exist
```

---

## 📖 Finding What You Need

**I want to...** → **Read this:**

| Need | Document |
|------|----------|
| Run the app NOW | [QUICK_COMMANDS.md](QUICK_COMMANDS.md) |
| Understand how it works | [ARCHITECTURE.md](ARCHITECTURE.md) |
| Follow step-by-step setup | [STARTUP_GUIDE.md](STARTUP_GUIDE.md) |
| Fix a problem | [STARTUP_GUIDE.md](STARTUP_GUIDE.md) - Troubleshooting |
| See all available commands | [QUICK_COMMANDS.md](QUICK_COMMANDS.md) |
| Know all the ports | [QUICK_COMMANDS.md](QUICK_COMMANDS.md) |
| Change database credentials | [STARTUP_GUIDE.md](STARTUP_GUIDE.md) - Configuration Changes |
| View request flow diagram | [ARCHITECTURE.md](ARCHITECTURE.md) |

---

## 🎯 What Each Service Does

### ServiceRegistry (Port 8761)
- Service discovery (where is each service?)
- Health monitoring (is the service alive?)
- Load balancing metadata
- Eureka Web UI for monitoring
- **Start:** `start-service-registry.bat`

### AdminMicroservice (Port 8081)
- Admin login and authentication
- Admin dashboard
- Admin user management
- JWT token generation
- **Start:** `start-admin-microservice.bat`

### API Gateway (Port 8080)
- Central entry point for all APIs
- Routes requests to appropriate microservice
- Load balancing
- Request/response logging
- **Start:** `start-api-gateway.bat`

### LoanMicroservice (Port 8082)
- Loan application processing
- Loan status checking
- Loan calculations (EMI, etc.)
- Loan data management
- **Start:** Manual: `.\mvnw.cmd spring-boot:run -pl LoanMicroservice`

---

## 🔄 Request Flow (30-second version)

```
1. User opens http://localhost:8080 in browser
2. Browser sends HTTP request to API Gateway
3. Gateway checks: "Is this an admin request or loan request?"
4. If admin → Route to AdminMicroservice (port 8081)
5. If loan → Route to LoanMicroservice (port 8082)
6. Service queries database (MySQL)
7. Service sends response back through gateway
8. Browser displays result to user
```

**Full details:** See [ARCHITECTURE.md](ARCHITECTURE.md)

---

## 🚀 Startup Checklist

Before running the application:

- [ ] MySQL is installed and running
- [ ] Java 17+ is installed (`java -version`)
- [ ] Ports 8761, 8081, 8080 are available
- [ ] You're in: `C:\Users\lohith\Desktop\Loan_application\demo`
- [ ] You have read access to all files

Before building:

- [ ] Run: `.\mvnw.cmd clean install -DskipTests` (first time only)
- [ ] Wait for "BUILD SUCCESS"

Before accessing the app:

- [ ] All services show "Tomcat started on port[X]"
- [ ] Eureka shows registered instances at http://localhost:8761

---

## 📞 Quick Reference

### Build Project
```powershell
.\mvnw.cmd clean install -DskipTests
```

### Start All Services
```powershell
& ".\start-all-services.bat"
```

### Check Services
```powershell
netstat -ano | findstr :876
```

### View Eureka
```
http://localhost:8761
```

### View Logs
Check the console window where service is running.

---

## 💡 Pro Tips

1. **Keep separate terminals open** for each service (easier to see logs)
2. **Use `start-all-services.bat`** for quick testing (opens all in one go)
3. **Check Eureka dashboard** (http://8761) to verify all services registered
4. **Watch for "Tomcat started on port X"** in logs (means service is ready)
5. **Use `netstat` command** to verify ports are listening
6. **Save these docs locally** for offline reference

---

## 📚 Documentation Map

```
README.md (you are here) ← Start here for overview
       ↓
QUICK_COMMANDS.md ← For copy-paste commands
       ↓
STARTUP_GUIDE.md ← For detailed step-by-step
       ↓
ARCHITECTURE.md ← For understanding the system
```

---

## ✅ Success Criteria

You'll know everything is working when:

1. ✅ `& ".\start-all-services.bat"` runs without errors
2. ✅ 4 console windows open (ServiceRegistry, Admin, Gateway, optional Loan)
3. ✅ Each shows "Tomcat started on port [X]"
4. ✅ http://localhost:8761 shows Eureka with 2+ instances
5. ✅ http://localhost:8081/admin-login shows the login page
6. ✅ `netstat -ano | findstr :8081` shows LISTENING

---

## 🆘 Need Help?

| Issue | Solution |
|-------|----------|
| Can't start services | Check [STARTUP_GUIDE.md](STARTUP_GUIDE.md) - Troubleshooting |
| Port conflicts | See "Port already in use" section above |
| MySQL errors | See "MySQL connection refused" section above |
| Don't understand flow | Read [ARCHITECTURE.md](ARCHITECTURE.md) |
| Lost? | Read this file again, Top to Bottom |

---

## 📝 File Descriptions

| File | Purpose | Last Updated |
|------|---------|--------------|
| STARTUP_GUIDE.md | Complete startup instructions | 2026-02-21 |
| QUICK_COMMANDS.md | Command reference | 2026-02-21 |
| ARCHITECTURE.md | System design & flows | 2026-02-21 |
| start-all-services.bat | Master startup script | 2026-02-21 |
| start-all-services.ps1 | PowerShell startup | 2026-02-21 |

---

## 🎓 Learning Path

1. **Read:** This file (README.md) - 5 min
2. **Run:** Start services using `start-all-services.bat` - 2 min
3. **Explore:** Visit http://localhost:8761 - 5 min
4. **Study:** Read [ARCHITECTURE.md](ARCHITECTURE.md) - 15 min
5. **Practice:** Try commands in [QUICK_COMMANDS.md](QUICK_COMMANDS.md) - 10 min

**Total time to understand: ~40 minutes**

---

## 📞 Quick Links

- **Eureka Dashboard:** http://localhost:8761
- **Admin Login:** http://localhost:8081/admin-login
- **API Gateway:** http://localhost:8080
- **MySQL:** localhost:3306 (admin_db)

---

**🎉 Ready? Let's start!**

```powershell
& "C:\Users\lohith\Desktop\Loan_application\demo\start-all-services.bat"
```

Then visit: http://localhost:8761

---

**Questions?** Check the appropriate documentation file above.
**First time?** Start with [QUICK_COMMANDS.md](QUICK_COMMANDS.md).
