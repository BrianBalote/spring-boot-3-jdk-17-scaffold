#!/bin/sh

# Stop and remove container if exists
podman rm -f sonarqube-mvn 2>/dev/null || true

echo "SonarQube container stopped/removed."