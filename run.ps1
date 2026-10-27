# =====================================================================
# CODE-ARENA Master Launcher (PowerShell)
# Usage: .\run.ps1 [web|java]
# =====================================================================
param (
    [string]$Target = "web"
)

$PSScriptRootPath = Split-Path -Parent $MyInvocation.MyCommand.Path

if ($Target -eq "web") {
    Write-Host "[CODE-ARENA] Launching Web Application (9-Screen UI System)..." -ForegroundColor Cyan
    Start-Process "http://localhost:3000/"
    Set-Location -Path "$PSScriptRootPath\web"
    & node server.js
}
elseif ($Target -eq "java") {
    Write-Host "[CODE-ARENA] Launching Java Swing Desktop App..." -ForegroundColor Cyan
    Set-Location -Path "$PSScriptRootPath\OnlineMCQQuiz"
    & .\run.ps1
}
else {
    Write-Host "Usage: .\run.ps1 [web|java]" -ForegroundColor Yellow
    Write-Host "  web  - Launch modern Web Application (Default)"
    Write-Host "  java - Launch Java Swing Desktop Application"
}
