#!/bin/sh

# 🔧 Get absolute project root directory
SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
ROOT_DIR="$(dirname "$SCRIPT_DIR")"
cd "$ROOT_DIR" || exit 1

echo "📦 Step 1: Committing changes in backend submodules..."

# Find all submodule paths under backend/
grep 'path = backend/' .gitmodules | cut -d'=' -f2 | sed 's/^[[:space:]]*//' | while read path; do
  echo "🔁 Checking $path..."
  if [ -d "$path" ]; then
    cd "$path" || exit 1
    if [ "$(git status --porcelain)" ]; then
      git add .
      git commit -m "backup before jhipster regeneration"
      echo "✅ Committed changes in $path"
    else
      echo "ℹ️  No changes to commit in $path"
    fi
    cd "$ROOT_DIR" || exit 1
  else
    echo "⚠️  Skipping $path (not found)"
  fi
done

echo "🛠 Step 2: Importing JDL into backend..."
cd "$ROOT_DIR/backend" || exit
jhipster import-jdl ../docs/jdl/app.jdl --force

echo "🚀 Step 3: Running setup script..."
cd "$ROOT_DIR/docs" || exit
./setup_apps.sh

echo "🔁 Step 4: Reverting selected changes in backend microservices..."

MS_SERVICES="ms_user ms_commentlike ms_reel ms_feed ms_notification"

for ms in $MS_SERVICES; do
  echo "🔄 Processing $ms..."
  clean_name=$(echo "$ms" | sed 's/^ms_//')
  service_path="$ROOT_DIR/backend/$ms"
  cd "$service_path" || continue

  # Revert specific folders
  for folder in web/rest service/impl repository; do
    git checkout -- "src/main/java/com/f4/${clean_name}/$folder/" 2>/dev/null || true
  done

  # Revert only top-level *.java in service/
  service_dir="src/main/java/com/f4/${clean_name}/service"
  if [ -d "$service_dir" ]; then
    for file in "$service_dir"/*.java; do
      [ -f "$file" ] && git checkout -- "$file" && echo "✅ Reverted $(basename "$file")"
    done
  else
    echo "⚠️  $service_dir not found"
  fi

  cd "$ROOT_DIR" || exit 1
done

echo "↩️ Step 5: Undo last commit in backend submodules (only if it was the backup commit)..."

grep 'path = backend/' .gitmodules | cut -d'=' -f2 | sed 's/^[[:space:]]*//' | while read path; do
  if [ -d "$path" ]; then
    cd "$path" || exit

    last_msg=$(git log -1 --pretty=%B)
    if echo "$last_msg" | grep -q "backup before jhipster regeneration"; then
      git reset --soft HEAD~1
      echo "✅ Undone backup commit in $path"
    else
      echo "ℹ️  No backup commit to undo in $path"
    fi

    cd "$ROOT_DIR" || exit
  fi
done


echo "✅ All done: backend submodules committed, JDL imported, setup completed, changes reverted, and commits undone conditionally."
