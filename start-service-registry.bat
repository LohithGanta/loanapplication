@echo off
echo Starting ServiceRegistry (Eureka Server) on port 8761...
cd /d "%~dp0"
call mvnw.cmd spring-boot:run -pl ServiceRegistry
