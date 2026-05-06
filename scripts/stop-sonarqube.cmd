@echo off
REM Stop and remove container if exists
podman rm -f sonarqube-mvn >nul 2>&1

echo SonarQube container stopped/removed.