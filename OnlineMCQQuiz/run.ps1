# =====================================================================
# Run script for Online MCQ Quiz System (PowerShell)
# Executes the Main application class with MySQL Connector on classpath
# =====================================================================

if (-not (Test-Path "bin\com\mcq\Main.class")) {
    Write-Host "Compiled binaries not found. Building first..." -ForegroundColor Yellow
    & .\build.ps1
}

Write-Host "Starting Online MCQ Quiz System..." -ForegroundColor Green
java -cp "bin;lib\mysql-connector-j-8.3.0.jar" com.mcq.Main
