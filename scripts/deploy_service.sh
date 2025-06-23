#!/bin/bash
# Correct deploy_service.sh script

# A script to update and rebuild a specific submodule service
# VERSION 2: Handles mapping from folder name (e.g., ms_user) to service name (e.g., user)

# Exit immediately if a command exits with a non-zero status.
set -e

# --- CONFIGURATION ---
# The FOLDER name passed from Jenkins (e.g., "ms_user", "gateway")
FOLDER_NAME=$1
# The directory of the main project on the VPS
PROJECT_DIR="/root/f4-microserices-vps-configuration/AppF4"

# --- VALIDATION ---
if [ -z "$FOLDER_NAME" ]; then
  echo "Error: No folder name provided. Please specify which submodule folder to update."
  echo "Usage: ./deploy_service.sh <folder_name>"
  exit 1
fi


# ===================================================================
# === NEW LOGIC: Determine the Docker Compose service name        ===
# ===================================================================
# This block translates the folder name into the correct service name for docker-compose.
# It checks if the folder name starts with "ms_". If it does, it removes that prefix.
# Otherwise, it uses the folder name as is (for services like 'gateway').

DOCKER_SERVICE_NAME=$FOLDER_NAME
if [[ "$FOLDER_NAME" == ms_* ]]; then
  DOCKER_SERVICE_NAME=${FOLDER_NAME#ms_} # This removes the 'ms_' prefix
fi
# ===================================================================

echo "--- Starting deployment ---"
echo "  > Git Folder:      $FOLDER_NAME"
echo "  > Docker Service:  $DOCKER_SERVICE_NAME"
echo "---------------------------"

cd $PROJECT_DIR

echo "1. Pulling latest changes from Git..."
git pull origin main
git submodule update --init --recursive --remote

ALLOWED_SERVICES=("gateway" "ms_user" "ms_feed" "ms_commentlike" "ms_notification" "ms_reel")

if [[ ! " ${ALLOWED_SERVICES[@]} " =~ " $FOLDER_NAME " ]]; then
  echo "⚠️ Folder '$FOLDER_NAME' is not a deployable service. Skipping build but continuing gracefully."
  exit 0
fi

# 2. Navigate to the specific submodule directory (using the FOLDER_NAME)
SUBMODULE_DIR="$PROJECT_DIR/backend/$FOLDER_NAME"
if [ ! -d "$SUBMODULE_DIR" ]; then
    echo "Error: Submodule directory not found at $SUBMODULE_DIR"
    exit 1
fi
cd $SUBMODULE_DIR
echo "Changed directory to $(pwd)"

# 3. Build the Docker image using Maven Jib
echo "2. Building Docker image with Maven Jib..."
chmod +x ./mvnw
./mvnw -ntp -Pprod verify jib:dockerBuild -DskipTests

# 4. Restart the specific service using Docker Compose (using the DOCKER_SERVICE_NAME)
echo "3. Restarting the Docker Compose service: $DOCKER_SERVICE_NAME..."
cd $PROJECT_DIR
docker-compose restart $DOCKER_SERVICE_NAME

echo "--- Deployment for service '$DOCKER_SERVICE_NAME' completed successfully! ---"
