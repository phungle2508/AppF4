#!/bin/bash
# Correct deploy_service.sh script

# A script to update and rebuild a specific submodule service
# VERSION 2: Handles mapping from folder name (e.g., ms_user) to service name (e.g., user)

set -e  # Exit immediately if a command exits with a non-zero status.

# --- CONFIGURATION ---
FOLDER_NAME=$1
PROJECT_DIR="/root/f4-microserices-vps-configuration/AppF4"

if [ -z "$FOLDER_NAME" ]; then
  echo "Error: No folder name provided. Please specify which submodule folder to update."
  echo "Usage: ./deploy_service.sh <folder_name>"
  exit 1
fi

# --- Derive Docker Compose service name ---
DOCKER_SERVICE_NAME=${FOLDER_NAME//_/}

echo "--- Starting deployment ---"
echo "  > Git Folder:      $FOLDER_NAME"
echo "  > Docker Service:  $DOCKER_SERVICE_NAME"
echo "---------------------------"

cd "$PROJECT_DIR"

echo "1. Pulling latest changes from Git..."
git pull origin main
git submodule update --init --recursive --remote

ALLOWED_SERVICES=("gateway" "ms_user" "ms_feed" "ms_commentlike" "ms_notification" "ms_reel")

if [[ ! " ${ALLOWED_SERVICES[@]} " =~ " $FOLDER_NAME " ]]; then
  echo "⚠️ Folder '$FOLDER_NAME' is not a deployable service. Skipping build but continuing gracefully."
  exit 0
fi

SUBMODULE_DIR="$PROJECT_DIR/backend/$FOLDER_NAME"
if [ ! -d "$SUBMODULE_DIR" ]; then
    echo "Error: Submodule directory not found at $SUBMODULE_DIR"
    exit 1
fi
cd "$SUBMODULE_DIR"
echo "Changed directory to $(pwd)"

# 2. Stop the service before building
echo "2. Stopping Docker Compose service '$DOCKER_SERVICE_NAME' before build..."
cd "$PROJECT_DIR"
docker compose stop "$DOCKER_SERVICE_NAME"
docker compose rm -f "$DOCKER_SERVICE_NAME"

# 3. Build Docker image
cd "$SUBMODULE_DIR"
echo "3. Building Docker image with Maven Jib..."
chmod +x ./mvnw
./mvnw -ntp -Pprod verify jib:dockerBuild -DskipTests

# 4. Start the service again
echo "4. Starting Docker Compose service '$DOCKER_SERVICE_NAME' after build..."
cd "$PROJECT_DIR"
docker compose up -d "$DOCKER_SERVICE_NAME"

echo "--- ✅ Deployment for service '$DOCKER_SERVICE_NAME' completed successfully! ---"
