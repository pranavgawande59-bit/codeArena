# =====================================================================
# Build script for Online MCQ Quiz System (PowerShell)
# Compiles all Java source files into the bin directory
# =====================================================================

Write-Host "[1/2] Creating bin directory..." -ForegroundColor Cyan
if (-not (Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" | Out-Null
}

Write-Host "[2/2] Compiling Java source files..." -ForegroundColor Cyan
$javaFiles = Get-ChildItem -Path "src" -Recurse -Filter *.java | ForEach-Object { $_.FullName }
$cp = "lib\mysql-connector-j-8.3.0.jar"

javac -cp $cp -d bin $javaFiles

if ($LASTEXITCODE -eq 0) {
    Write-Host "`n=========================================================" -ForegroundColor Green
    Write-Host "Build Successful! Run .\run.ps1 or .\run.bat to start." -ForegroundColor Green
    Write-Host "=========================================================" -ForegroundColor Green
} else {
    Write-Host "`n=========================================================" -ForegroundColor Red
    Write-Host "Compilation Failed! Check Java errors above." -ForegroundColor Red
    Write-Host "=========================================================" -ForegroundColor Red
}
