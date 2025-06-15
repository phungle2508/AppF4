#!/bin/bash

# A script to update and rebuild a specific submodule service

# Exit immediately if a command exits with a non-zero status.
set -e

# --- CONFIGURATION ---
# The service name passed from Jenkins (e.g., "user", "product")
SERVICE_NAME=$1
# The directory of the main project on the VPS
PROJECT_DIR="/root/f4-microserices-vps-configuration/AppF4"

# --- VALIDATION ---
if [ -z "$SERVICE_NAME" ]; then
  echo "Error: No service name provided. Please specify which submodule to update."
  echo "Usage: ./deploy_service.sh <service_name>"
  exit 1
fi

echo "--- Starting deployment for service: $SERVICE_NAME ---"

# --- SCRIPT LOGIC ---
# Navigate to the main project directory
cd $PROJECT_DIR

# 1. Update the source code for the main repo and all submodules
echo "1. Pulling latest changes from Git..."
git pull origin master # Or 'main' depending on your branch name
git submodule update --init --recursive --remote

# 2. Navigate to the specific submodule directory
SUBMODULE_DIR="$PROJECT_DIR/backend/$SERVICE_NAME"
if [ ! -d "$SUBMODULE_DIR" ]; then
    echo "Error: Submodule directory not found at $SUBMODULE_DIR"
    exit 1
fi
cd $SUBMODULE_DIR
echo "Changed directory to $(pwd)"

# 3. Build the Docker image using Maven Jib
# This builds the image directly into the local Docker daemon, no push/pull needed.
echo "2. Building Docker image with Maven Jib..."
# Ensure the Maven wrapper is executable
chmod +x ./mvnw
./mvnw -ntp -Pprod verify jib:dockerBuild -DskipTests

# 4. Restart the specific service using Docker Compose
# The docker-compose.yml file is in the parent directory.
echo "3. Restarting the Docker Compose service: $SERVICE_NAME..."
cd $PROJECT_DIR
docker-compose restart $SERVICE_NAME

echo "--- Deployment for service '$SERVICE_NAME' completed successfully! ---"