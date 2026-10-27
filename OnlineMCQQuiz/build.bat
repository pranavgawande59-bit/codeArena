@echo off
REM =====================================================================
REM Build script for Online MCQ Quiz System
REM Delegates compilation to build.ps1 for robust path handling
REM =====================================================================

powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0build.ps1"
