@echo off
echo Starting UserMicroservice on port 8082...
timeout /t 3 /nobreak
cd UserMicroservice
call ..\mvnw.cmd spring-boot:run
