#!/bin/bash

# Step 1: Backup all submodules
echo "📦 Backing up submodules before regeneration..."
git config --file .gitmodules --get-regexp path | awk '{print $2}' | grep '^backend/' | while read -r path; do
  echo "🔄 Committing changes in $path"
  if [ -d "$path/.git" ]; then
    git -C "$path" add .
    git -C "$path" commit -m "backup before jhipster regeneration" || echo "ℹ️  No changes in $path"
  else
    echo "⚠️  $path is not a valid Git submodule"
  fi
done

# Step 2: Import JDL in backend
echo "🛠 Importing JDL..."
cd ../backend || exit
jhipster import-jdl ../docs/jdl/app.jdl --force

# Step 3: Run setup script
echo "🚀 Running setup script..."
cd ../docs
./setup_apps.sh

# Step 4: Revert specific changes in each microservice
echo "🔁 Reverting selected changes in microservices..."

declare -a ms_services=("ms_user" "ms_commentlike" "ms_reel" "ms_feed" "ms_notification")

get_clean_name() {
    echo "${1#ms_}"
}
cd ../
for ms in "${ms_services[@]}"; do
    echo "🔄 Processing $ms..."
    clean_name=$(get_clean_name "$ms")
    cd "backend/$ms" || exit

    # Revert web/rest, service/impl, repository, and liquibase folders
    git checkout -- "src/main/java/com/f4/${clean_name}/web/rest/" 2>/dev/null
    git checkout -- "src/main/java/com/f4/${clean_name}/service/impl/" 2>/dev/null
    git checkout -- "src/main/java/com/f4/${clean_name}/repository/" 2>/dev/null
    git checkout -- "src/main/resources/config/liquibase/" 2>/dev/null

    # Revert only top-level service/*.java files (not subfolders)
    service_dir="src/main/java/com/f4/${clean_name}/service"
    if [ -d "$service_dir" ]; then
        for file in "$service_dir"/*.java; do
            if [ -f "$file" ]; then
                git checkout -- "$file"
                echo "✅ Reverted $(basename "$file")"
            fi
        done
    else
        echo "⚠️  $service_dir not found"
    fi

    cd - > /dev/null || exit
done

echo "✅ Done: selective reverts completed after regeneration."
