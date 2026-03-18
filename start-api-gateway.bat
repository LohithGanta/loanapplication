@echo off
setlocal enabledelayedexpansion

echo ========================================
echo Starting API Gateway on port 8080
echo ========================================

echo Prerequisites:
echo  - ServiceRegistry must be running on port 8761
echo  - AdminMicroservice should be running on port 8081

timeout /t 3
cd /d "%~dp0"

echo Starting API Gateway...
call mvnw.cmd spring-boot:run -pl ApiGateway

echo API Gateway stopped.
pause
endlocal
