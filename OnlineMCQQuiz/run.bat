@echo off
REM =====================================================================
REM Run script for Online MCQ Quiz System
REM Executes the Main application class with MySQL Connector on classpath
REM =====================================================================

if not exist "bin\com\mcq\Main.class" (
    echo Compiled binaries not found. Building first...
    call build.bat
)

echo Starting Online MCQ Quiz System...
java -cp "bin;lib\mysql-connector-j-8.3.0.jar" com.mcq.Main
