#!/bin/bash

cd ../backend || exit

# Prompt for commit message
echo "Enter commit message (or press Enter for default):"
read -r commit_message

# Use default message if none provided
if [ -z "$commit_message" ]; then
  commit_message="Update: $(date '+%Y-%m-%d %H:%M:%S') liquibase init csv"
fi

declare -A repos=(
  ["ms_user"]="https://github.com/shegga9x/f4-user-service.git"
  ["ms_commentlike"]="https://github.com/shegga9x/f4-commentlike-service.git"
  ["ms_reel"]="https://github.com/shegga9x/f4-reel-service.git"
  ["ms_feed"]="https://github.com/shegga9x/f4-feed-service.git"
  ["ms_notification"]="https://github.com/shegga9x/f4-notification-service.git"
  ["gateway"]="https://github.com/shegga9x/f4-gateway-service"
)

for folder in "${!repos[@]}"; do
  echo "-----"
  echo "📦 Committing and pushing $folder"

  if [ -d "$folder" ]; then
    cd "$folder" || continue

    # Ensure we're not in a detached HEAD
    current_branch=$(git symbolic-ref --short -q HEAD)
    if [ -z "$current_branch" ]; then
      echo "🔄 HEAD is detached. Checking out or creating 'master' branch..."
      git checkout master 2>/dev/null || git checkout -b master origin/master
    fi

    # Check for changes
    if [ -n "$(git status --porcelain)" ]; then
      git add .
      git commit -m "$commit_message"
      git push origin master
      echo "✅ Changes pushed for $folder"
    else
      echo "🟡 No changes to commit in $folder"
    fi

    cd ..
  else
    echo "❌ $folder directory not found. Skipping."
  fi
done
