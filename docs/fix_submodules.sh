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

# First, let's remove existing submodules from .git
git config -f .git/config --remove-section submodule.backend/gateway 2>/dev/null || true
git config -f .git/config --remove-section submodule.backend/ms_user 2>/dev/null || true
git config -f .git/config --remove-section submodule.backend/ms_commentlike 2>/dev/null || true
git config -f .git/config --remove-section submodule.backend/ms_reel 2>/dev/null || true
git config -f .git/config --remove-section submodule.backend/ms_feed 2>/dev/null || true
git config -f .git/config --remove-section submodule.backend/ms_notification 2>/dev/null || true

# Remove the backend directory and recreate it
rm -rf backend/
mkdir -p backend/

# Re-initialize all submodules
git submodule init
git submodule update

# For each service, set up as a proper submodule
for service_dir in "${!backend_services[@]}"; do
    repo_name="${backend_services[$service_dir]}"
    echo -e "\n${BLUE}Setting up $repo_name as submodule...${NC}"
    
    git submodule add -f "https://github.com/shegga9x/$repo_name.git" "backend/$service_dir"
    
    # Initialize the submodule
    cd "backend/$service_dir"
    if [ ! -f "README.md" ]; then
        echo "# $repo_name" > README.md
        git add README.md
        git commit -m "Add README"
        git push
    fi
    cd ../../
done

# Commit all changes
git add .gitmodules backend/
git commit -m "Fix backend submodules"
git push

echo -e "\n${GREEN}All backend services have been properly set up as submodules!${NC}" 