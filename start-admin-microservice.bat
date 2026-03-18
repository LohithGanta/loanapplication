@echo off
setlocal enabledelayedexpansion

echo.
echo ========================================
echo Starting AdminMicroservice on port 8081
echo ========================================
echo.
echo Prerequisites:
echo  - ServiceRegistry must be running on port 8761
echo  - MySQL must be running (root/0205)
echo.

timeout /t 3

cd /d "%~dp0"

echo Setting environment variables...
set SPRING_DATASOURCE_USERNAME=root
set SPRING_DATASOURCE_PASSWORD=0205

echo Starting application...
echo.

call mvnw.cmd spring-boot:run -pl AdminMicroservice

echo.
echo AdminMicroservice stopped.
pause
endlocal
