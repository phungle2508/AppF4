#!/bin/bash
set -e

# --- Configuration ---
BASE_URL="${BASE_URL:-https://appf4.io.vn}"
KEYCLOAK_URL="https://keycloak.appf4.io.vn/realms/jhipster/protocol/openid-connect/token"
CLIENT_ID="web_app"
CLIENT_SECRET="your_client_secret_here"
USERNAME="admin"
PASSWORD="admin"
declare -a SERVICE_NAMES=( "msfeed" "msreel" "msnotification" "msuser" "mscommentlike")

# Script-relative directories
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
OUTPUT_DIR="$SCRIPT_DIR/output"
POSTMAN_COLLECTIONS_DIR="$OUTPUT_DIR/all_collections"
TEMP_POSTMAN_DIR="$OUTPUT_DIR/temp_postman"
JAVA_CLIENT_OUT_DIR="$OUTPUT_DIR/generated-java-client"
TS_CLIENT_OUT_DIR="$SCRIPT_DIR/../../microfrontend/services/api-client/src"

JAVA_API_PACKAGE="com.f4.reel.client.api"
JAVA_MODEL_PACKAGE="com.f4.reel.client.model"

# --- Prerequisite Check ---
check_dependencies() {
  echo "🔍 Checking for required tools..."
  for cmd in jq openapi-generator-cli node; do
    if ! command -v "$cmd" &>/dev/null; then
      echo "✖ '$cmd' is not installed. Please install it."
      exit 1
    fi
  done
  echo "✔ All dependencies OK."
}

# --- Get Token ---
get_token() {
  echo "🔑 Getting Keycloak token..."
  local response
  response=$(curl -s -X POST "$KEYCLOAK_URL" \
    -d "client_id=$CLIENT_ID" \
    -d "client_secret=$CLIENT_SECRET" \
    -d "username=$USERNAME" \
    -d "password=$PASSWORD" \
    -d "grant_type=password")
  TOKEN=$(jq -r '.access_token' <<< "$response")

  if [ -z "$TOKEN" ] || [ "$TOKEN" == "null" ]; then
    echo "✖ Failed to get token. Response: $response"
    exit 1
  fi
  echo "✔ Token acquired."
}

# --- Fetch Spec ---
fetch_openapi_spec() {
  local svc="$1"
  local spec_file="$OUTPUT_DIR/openapi_spec_${svc}.json"
  echo "🌐 Fetching spec for $svc..."

  local http_code
  http_code=$(curl -s -w "%{http_code}" -o "$spec_file" \
    -H "Authorization: Bearer $TOKEN" \
    "$BASE_URL/services/$svc/v3/api-docs")

  if [ "$http_code" != "200" ]; then
    echo "✖ HTTP $http_code fetching $svc"
    rm -f "$spec_file"
    return 1
  fi

  echo "✔ Downloaded spec: $spec_file"
  return 0
}

# --- Merge Specs ---
merge_openapi_specs() {
  local merged_spec="$OUTPUT_DIR/openapi_spec_all.json"
  echo "🔗 Merging OpenAPI specs..."

  local specs=()
  for svc in "${SERVICE_NAMES[@]}"; do
    local f="$OUTPUT_DIR/openapi_spec_${svc}.json"
    [ -f "$f" ] && specs+=("$f")
  done

  if [ ${#specs[@]} -eq 0 ]; then
    echo "✖ No specs found. Aborting."
    exit 1
  fi

  cp "${specs[0]}" "$merged_spec"
  for ((i = 1; i < ${#specs[@]}; i++)); do
    jq -s '
      .[0] as $base | .[1] as $new | $base
      | .paths += ($new.paths // {})
      | .components.schemas += ($new.components.schemas // {})
      | .components.responses += ($new.components.responses // {})
      | .components.parameters += ($new.components.parameters // {})
      | .components.requestBodies += ($new.components.requestBodies // {})
      | .components.securitySchemes += ($new.components.securitySchemes // {})
    ' "$merged_spec" "${specs[$i]}" > "$OUTPUT_DIR/tmp.json" && mv "$OUTPUT_DIR/tmp.json" "$merged_spec"
  done

  echo "✔ Merged spec: $merged_spec"
}

# --- Generate TypeScript Client ---
generate_ts_client() {
  echo "🧬 Generating TS Axios client..."
  openapi-generator-cli generate \
    -i "$OUTPUT_DIR/openapi_spec_all.json" \
    -g typescript-axios \
    -o "$TS_CLIENT_OUT_DIR" \
    --skip-validate-spec \
    --additional-properties=supportsES6=true,withSeparateModelsAndApi=true,apiPackage=api,modelPackage=model
  echo "✔ TS client output to $TS_CLIENT_OUT_DIR"
}

# --- Generate Java Client ---
generate_java_feign_client() {
  echo "🧬 Generating Java Feign client..."
  openapi-generator-cli generate \
    -i "$OUTPUT_DIR/openapi_spec_all.json" \
    -g java \
    -o "$JAVA_CLIENT_OUT_DIR" \
    --library feign \
    --skip-validate-spec \
    --api-package "$JAVA_API_PACKAGE" \
    --model-package "$JAVA_MODEL_PACKAGE" \
    --additional-properties=interfaceOnly=true,dateLibrary=java8,useJakartaEe=false
  echo "✔ Java client output to $JAVA_CLIENT_OUT_DIR"
}

# --- Main ---
main() {
  echo "🚀 Starting OpenAPI client generation..."
  mkdir -p "$POSTMAN_COLLECTIONS_DIR" "$JAVA_CLIENT_OUT_DIR" "$TEMP_POSTMAN_DIR"
  check_dependencies
  get_token

  for svc in "${SERVICE_NAMES[@]}"; do
    echo "--- 🔄 Processing: $svc ---"
    if ! fetch_openapi_spec "$svc"; then
      echo "⚠️ Skipping $svc"
      continue
    fi

    echo "📝 Generating Postman for $svc..."
    local col_file="$TEMP_POSTMAN_DIR/$svc/postman.json"
    openapi-generator-cli generate \
      -i "$OUTPUT_DIR/openapi_spec_${svc}.json" \
      -g postman-collection \
      -o "$TEMP_POSTMAN_DIR/$svc" \
      --skip-validate-spec

    [ -f "$SCRIPT_DIR/addAuthorizationHeader.js" ] && node "$SCRIPT_DIR/addAuthorizationHeader.js" "$col_file"
    [ -f "$SCRIPT_DIR/groupByClass.js" ] && node "$SCRIPT_DIR/groupByClass.js" "$col_file"

    jq '.info.schema = "https://schema.postman.com/json/collection/v2.1.0/collection.json"' "$col_file" > "$TEMP_POSTMAN_DIR/tmp.json" && mv "$TEMP_POSTMAN_DIR/tmp.json" "$col_file"

    mv "$col_file" "$POSTMAN_COLLECTIONS_DIR/${svc}.postman.json"
    echo "✔ Saved Postman collection: ${svc}.postman.json"
  done

  echo "--- 🔗 Merging + Generating Clients ---"
  merge_openapi_specs
  generate_ts_client
  generate_java_feign_client

  [ -f "$SCRIPT_DIR/rewriteUrls.js" ] && node "$SCRIPT_DIR/rewriteUrls.js"

  echo "🧹 Cleaning up temp files..."
  rm -rf "$TEMP_POSTMAN_DIR"
  rm -f "$OUTPUT_DIR"/openapi_spec_*.json

  echo "📦 Copying Java client to docs/template/client..."
  TARGET_DIR="$SCRIPT_DIR/../../docs/template"
  mkdir -p "$TARGET_DIR"
  cp -r "$JAVA_CLIENT_OUT_DIR/src/main/java/com/f4/reel/client" "$TARGET_DIR"
  echo "✔ Java client copied to $TARGET_DIR"

  echo "✅ Done! Output in: $OUTPUT_DIR"
}

main
