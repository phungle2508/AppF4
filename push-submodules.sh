#!/bin/bash

cd backend

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
    cd "$folder"

    # Check for changes
    if [ -n "$(git status --porcelain)" ]; then
      git add .
      git commit -m "Update: $(date '+%Y-%m-%d %H:%M:%S')"
      git push origin HEAD:main
      echo "✅ Changes pushed for $folder"
    else
      echo "🟡 No changes to commit in $folder"
    fi

    cd ..
  else
    echo "❌ $folder directory not found. Skipping."
  fi
done
