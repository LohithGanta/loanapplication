@echo off
echo Starting LoanMicroservice on port 8084...
timeout /t 3 /nobreak
cd LoanMicroservice
call ..\mvnw.cmd spring-boot:run
