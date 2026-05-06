@echo off
REM Remove any existing container with this name
podman rm -f sonarqube-mvn >nul 2>&1

REM Run SonarQube container
podman run -d --name sonarqube-mvn -p 9000:9000 ^
  -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true ^
  sonarqube:latest

echo SonarQube container started. URL: http://localhost:9000