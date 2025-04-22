#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Backend services configuration
declare -A backend_services=(
    ["gateway"]="f4-gateway-service"
    ["ms_user"]="f4-user-service"
    ["ms_commentlike"]="f4-commentlike-service"
    ["ms_reel"]="f4-reel-service"
    ["ms_feed"]="f4-feed-service"
    ["ms_notification"]="f4-notification-service"
)

# Check if gh CLI is installed
if ! command -v gh &> /dev/null; then
    echo -e "${RED}GitHub CLI (gh) is not installed. Please install it first.${NC}"
    exit 1
fi

# Check if logged in to GitHub
if ! gh auth status &> /dev/null; then
    echo -e "${RED}Not logged in to GitHub. Please run 'gh auth login' first.${NC}"
    exit 1
fi

# Create backend directory if it doesn't exist
mkdir -p backend

# Function to setup a service
setup_service() {
    local service_dir="$1"
    local repo_name="$2"
    
    echo -e "\n${BLUE}Setting up $repo_name...${NC}"
    
    # Check if repository exists
    if ! gh repo view "shegga9x/$repo_name" &> /dev/null; then
        echo -e "${BLUE}Creating repository $repo_name...${NC}"
        gh repo create "shegga9x/$repo_name" --public --description "F4 Backend Service" --confirm
    fi
    
    # Initialize service directory if it doesn't exist
    if [ ! -d "backend/$service_dir" ]; then
        mkdir -p "backend/$service_dir"
        echo "# $repo_name" > "backend/$service_dir/README.md"
        
        # Initialize git repository
        cd "backend/$service_dir"
        git init
        git add .
        git commit -m "Initial commit"
        git branch -M main
        git remote add origin "https://github.com/shegga9x/$repo_name.git"
        git push -u origin main
        cd ../../
    fi
    
    # Add as submodule if not already
    if [ ! -f ".gitmodules" ] || ! grep -q "\\[submodule \"backend/$service_dir\"\\]" .gitmodules; then
        git submodule add "https://github.com/shegga9x/$repo_name.git" "backend/$service_dir"
    fi
}

# Setup each service
for service_dir in "${!backend_services[@]}"; do
    setup_service "$service_dir" "${backend_services[$service_dir]}"
done

# Commit changes
git add .gitmodules backend/
git commit -m "Add backend services as submodules"
git push

echo -e "\n${GREEN}All backend services have been set up as submodules!${NC}" 