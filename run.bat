@echo off
REM =====================================================================
REM CODE-ARENA Master Launcher
REM Supports launching both Web UI (9-Screen System) and Java Desktop App
REM =====================================================================

set TARGET=%1
if "%TARGET%"=="" set TARGET=web

if /I "%TARGET%"=="web" (
    echo [CODE-ARENA] Starting Web Application...
    start http://localhost:3000/
    cd /d "%~dp0web"
    node server.js
    goto :eof
)

if /I "%TARGET%"=="java" (
    echo [CODE-ARENA] Starting Java Swing Desktop App...
    cd /d "%~dp0OnlineMCQQuiz"
    call run.bat
    goto :eof
)

echo Usage: run.bat [web|java]
echo   web  - Launch modern Web Application (Default)
echo   java - Launch Java Swing Desktop Application
