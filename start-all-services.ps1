# Loan Application - PowerShell Startup Script
# Usage: .\start-all-services.ps1

param(
    [switch]$SkipMySQLCheck = $false,
    [switch]$OpenBrowser = $true
)

$ErrorActionPreference = "Stop"
$projectRoot = (Get-Location).Path
$startTime = Get-Date

function Write-Header([string]$text) {
    Write-Host "`n" -NoNewline
    Write-Host "╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
    Write-Host "║ $($text.PadRight(62)) ║" -ForegroundColor Cyan
    Write-Host "╚════════════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
}

function Write-Step([string]$number, [string]$text) {
    Write-Host "`n[$number] $text" -ForegroundColor Yellow
    Write-Host "─────────────────────────────────────────────────────────────────" -ForegroundColor Gray
}

function Write-Success([string]$text) {
    Write-Host "✓ $text" -ForegroundColor Green
}

function Write-Error-Custom([string]$text) {
    Write-Host "✗ ERROR: $text" -ForegroundColor Red
}

function Write-Info([string]$text) {
    Write-Host "[i] $text" -ForegroundColor Cyan
}

# Main Script
Clear-Host

Write-Header "LOAN APPLICATION - COMPLETE MICROSERVICES STARTUP"

# Step 1: Verify MySQL
Write-Step "1/4" "Verifying MySQL connectivity..."

if (-not $SkipMySQLCheck) {
    try {
        $mysqlCheck = mysql -u root -p0205 -e "SELECT 'MySQL OK' as status;" 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-Success "MySQL is running and accessible on port 3306"
        } else {
            Write-Error-Custom "MySQL is not accessible with credentials root/0205"
            Write-Host "  Please start MySQL Service and try again."
            pause
            exit 1
        }
    } catch {
        Write-Host "⚠ MySQL check skipped (mysql.exe not found in PATH)`nContinuing anyway..." -ForegroundColor Yellow
    }
} else {
    Write-Info "MySQL check skipped (--SkipMySQLCheck flag)"
}

# Step 2: Build Project
Write-Step "2/4" "Building all modules..."
try {
    cd $projectRoot
    & .\mvnw.cmd clean install -DskipTests -q
    if ($LASTEXITCODE -ne 0) {
        Write-Error-Custom "Build failed. Run build manually to see errors."
        pause
        exit 1
    }
    Write-Success "Build completed successfully"
} catch {
    Write-Error-Custom "Build process failed: $_"
    pause
    exit 1
}

# Step 3: Start Services
Write-Step "3/4" "Starting microservices..."

# ServiceRegistry
Write-Host "  Starting ServiceRegistry on port 8761..." -ForegroundColor Cyan
Start-Process -FilePath "cmd.exe" -ArgumentList "/k `"cd /d `"$projectRoot`" && mvnw.cmd spring-boot:run -pl ServiceRegistry`"" -WindowStyle Normal
Start-Sleep -Seconds 6

# AdminMicroservice
Write-Host "  Starting AdminMicroservice on port 8081..." -ForegroundColor Cyan
$env:SPRING_DATASOURCE_USERNAME = "root"
$env:SPRING_DATASOURCE_PASSWORD = "0205"
Start-Process -FilePath "cmd.exe" -ArgumentList "/k `"cd /d `"$projectRoot`" && mvnw.cmd spring-boot:run -pl AdminMicroservice`"" -WindowStyle Normal
Start-Sleep -Seconds 6

# API Gateway
Write-Host "  Starting API Gateway on port 8080..." -ForegroundColor Cyan
Start-Process -FilePath "cmd.exe" -ArgumentList "/k `"cd /d `"$projectRoot`" && mvnw.cmd spring-boot:run -pl ApiGateway`"" -WindowStyle Normal
Start-Sleep -Seconds 3

# Step 4: Display Summary
Write-Step "4/4" "Startup Summary"

Write-Host "`n╔════════════════════════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║                   ALL SERVICES STARTED                          ║" -ForegroundColor Green
Write-Host "╠════════════════════════════════════════════════════════════════╣" -ForegroundColor Green
Write-Host "║ Service              Port    URL                               ║" -ForegroundColor Green
Write-Host "├────────────────────────────────────────────────────────────────┤" -ForegroundColor Green
Write-Host "║ ServiceRegistry      8761    http://localhost:8761             ║" -ForegroundColor Green
Write-Host "║ AdminMicroservice    8081    http://localhost:8081             ║" -ForegroundColor Green
Write-Host "║ API Gateway          8080    http://localhost:8080             ║" -ForegroundColor Green
Write-Host "║ MySQL Database       3306    admin_db                          ║" -ForegroundColor Green
Write-Host "╚════════════════════════════════════════════════════════════════╝" -ForegroundColor Green

# Open Browsers
if ($OpenBrowser) {
    Write-Info "Opening browser windows..."
    Start-Sleep -Seconds 2
    
    Start-Process "http://localhost:8761"
    Start-Sleep -Seconds 1
    Start-Process "http://localhost:8081/admin-login"
    Start-Sleep -Seconds 1
    Start-Process "http://localhost:8080"
}

# Completion
$duration = (Get-Date) - $startTime
Write-Host "`n✓ All services started successfully in $($duration.TotalSeconds) seconds!`n" -ForegroundColor Green
Write-Host "📋 Service Logs:" -ForegroundColor Yellow
Write-Info "ServiceRegistry console shows Eureka startup"
Write-Info "AdminMicroservice console shows database connection"
Write-Info "API Gateway console shows routing configuration"
Write-Host "`n🛑 To Stop Services:" -ForegroundColor Yellow
Write-Info "Close each console window or press Ctrl+C in any window to stop that service"
Write-Info "Or use: Get-Process java | Stop-Process -Force (stops all Java processes)"
Write-Host "`n📚 Full Documentation:" -ForegroundColor Yellow
Write-Info "See STARTUP_GUIDE.md for troubleshooting and detailed commands"

Read-Host "`nPress Enter to exit"
