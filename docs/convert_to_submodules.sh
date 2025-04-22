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

# Function to convert a directory to a submodule
convert_to_submodule() {
    local service_dir="$1"
    local repo_name="$2"
    
    echo -e "\n${BLUE}Converting $service_dir to submodule...${NC}"
    
    # Check if directory exists
    if [ ! -d "backend/$service_dir" ]; then
        echo -e "${RED}Directory backend/$service_dir does not exist. Skipping.${NC}"
        return
    fi
    
    # Create temporary directory for the service
    local temp_dir=$(mktemp -d)
    echo -e "${BLUE}Created temporary directory $temp_dir${NC}"
    
    # Copy contents to temporary directory
    cp -r "backend/$service_dir/"* "$temp_dir/" 2>/dev/null || true
    
    # Check if repository exists
    if ! gh repo view "shegga9x/$repo_name" &> /dev/null; then
        echo -e "${BLUE}Creating repository $repo_name...${NC}"
        gh repo create "shegga9x/$repo_name" --public --description "F4 Backend Service" --confirm
    else
        echo -e "${BLUE}Repository $repo_name already exists${NC}"
    fi
    
    # Remove directory from main repository
    rm -rf "backend/$service_dir"
    
    # Add as submodule
    git submodule add "https://github.com/shegga9x/$repo_name.git" "backend/$service_dir"
    
    # Copy contents back to submodule
    cp -r "$temp_dir/"* "backend/$service_dir/" 2>/dev/null || true
    
    # Initialize and push the submodule
    cd "backend/$service_dir"
    git add .
    
    # Only commit if there are changes
    if git status | grep -q "Changes to be committed"; then
        git commit -m "Initial commit"
        git push -u origin main
    else
        echo -e "${BLUE}No changes to commit in $service_dir${NC}"
    fi
    
    cd ../../
    
    # Clean up temporary directory
    rm -rf "$temp_dir"
    echo -e "${GREEN}Successfully converted $service_dir to submodule${NC}"
}

# For each service, convert it to a submodule
for service_dir in "${!backend_services[@]}"; do
    convert_to_submodule "$service_dir" "${backend_services[$service_dir]}"
done

# Commit changes to main repository
git add .gitmodules backend/
git commit -m "Convert backend services to submodules"
git push

echo -e "\n${GREEN}All backend services have been converted to submodules!${NC}" 