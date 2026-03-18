@echo off
setlocal enabledelayedexpansion

color 0A
cls

echo.
echo ╔════════════════════════════════════════════════════════════════╗
echo ║        LOAN APPLICATION - COMPLETE MICROSERVICES STARTUP       ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.

echo [Step 1 of 4] Verifying MySQL connectivity...
echo ─────────────────────────────────────────────────────────────────
mysql -u root -p0205 -e "SELECT 'MySQL OK' as status;" >nul 2>&1
if errorlevel 1 (
    echo ✗ ERROR: MySQL is not accessible (root/0205)
    echo   Please start MySQL Service and try again.
    echo.
    pause
    exit /b 1
)
echo ✓ MySQL is running and accessible

echo.
echo [Step 2 of 4] Starting ServiceRegistry (Eureka) on port 8761...
echo ─────────────────────────────────────────────────────────────────
cd /d "%~dp0"
start "ServiceRegistry" cmd /k "mvnw.cmd spring-boot:run -pl ServiceRegistry"
timeout /t 6

echo.
echo [Step 3 of 4] Starting AdminMicroservice on port 8081...
echo ─────────────────────────────────────────────────────────────────
set SPRING_DATASOURCE_USERNAME=root
set SPRING_DATASOURCE_PASSWORD=0205
start "AdminMicroservice" cmd /k "mvnw.cmd spring-boot:run -pl AdminMicroservice"
timeout /t 6

echo.
echo [Step 4 of 4] Starting API Gateway on port 8080...
echo ─────────────────────────────────────────────────────────────────
start "API Gateway" cmd /k "mvnw.cmd spring-boot:run -pl ApiGateway"
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
echo ║ MySQL Database       3306    admin_db (root/0205)              ║
echo ╚════════════════════════════════════════════════════════════════╝
echo.
echo Opening frontpages in browser...
timeout /t 2

start http://localhost:8761
timeout /t 1
start http://localhost:8081/admin-login
timeout /t 1
start http://localhost:8080

echo.
echo ✓ All services started successfully!
echo.
echo   Eureka Dashboard:     http://localhost:8761
echo   Admin Login:          http://localhost:8081/admin-login
echo   API Gateway:          http://localhost:8080
echo.
echo [i] Check console windows for service logs
echo [i] Press Ctrl+C in any window to stop that service
echo [i] To stop all: Close each console window or use Task Manager
echo.
pause
endlocal
