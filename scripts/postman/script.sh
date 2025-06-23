#!/bin/bash
#
# This script automates the generation of Postman collections, TypeScript, and
# Java Feign API clients from multiple microservice OpenAPI specifications.
#
# Flow:
# 1. Obtains a Keycloak auth token.
# 2. For each service:
#    a. Fetches its individual OpenAPI spec.
#    b. Generates and processes a service-specific Postman collection.
# 3. Merges all individual OpenAPI specs into one unified file.
# 4. Generates a unified TypeScript Axios client from the merged spec.
# 5. Generates a unified Java Feign client from the merged spec.
# 6. Cleans up intermediate files.

# --- Configuration ---
set -e # Exit immediately if a command exits with a non-zero status.

# Base URL for your Swagger API docs
BASE_URL="${BASE_URL:-https://appf4.io.vn}"

# Keycloak credentials and endpoint
KEYCLOAK_URL="https://keycloak.appf4.io.vn/realms/jhipster/protocol/openid-connect/token"
CLIENT_ID="web_app"
CLIENT_SECRET="your_client_secret_here"
USERNAME="admin"
PASSWORD="admin"

# Services to generate clients for
declare -a SERVICE_NAMES=("msfeed" "msreel" "msnotification" "msuser" "mscommentlike") 

# Output Directories
POSTMAN_COLLECTIONS_DIR="./all_collections"
TS_CLIENT_OUT_DIR="../../microfrontend/services/api-client/src"
JAVA_CLIENT_OUT_DIR="./generated-java-client"
JAVA_API_PACKAGE="com.f4.reel.client.api"
JAVA_MODEL_PACKAGE="com.f4.reel.client.model"


# --- Prerequisite Check ---
check_dependencies() {
  echo "Checking for required tools..."
  if ! command -v jq &> /dev/null; then
    echo "✖ 'jq' is not installed. Please install it to continue."
    exit 1
  fi
  if ! command -v openapi-generator-cli &> /dev/null; then
    echo "✖ 'openapi-generator-cli' is not installed. Please install it to continue."
    exit 1
  fi
  if ! command -v node &> /dev/null; then
    echo "✖ 'node' is not installed. Please install your Node.js helpers to continue."
    exit 1
  fi
  echo "✔ All dependencies are present."
}


# --- Functions ---

# 1) Obtain Keycloak token
get_token() {
  echo "Getting Keycloak token..."
  local token_response
  token_response=$(curl -s -X POST "$KEYCLOAK_URL" \
    -d "client_id=$CLIENT_ID" \
    -d "client_secret=$CLIENT_SECRET" \
    -d "username=$USERNAME" \
    -d "password=$PASSWORD" \
    -d "grant_type=password")
  
  TOKEN=$(jq -r '.access_token' <<<"$token_response")
  
  if [ -z "$TOKEN" ] || [ "$TOKEN" == "null" ]; then
    echo "✖ Could not fetch token. Response: $token_response"
    exit 1
  fi
  echo "✔ Token acquired."
}

# 2) Fetch and validate OpenAPI spec for a single service
fetch_openapi_spec() {
  local svc="$1"
  local spec_file="openapi_spec_${svc}.json"
  echo "Fetching spec for $svc..."
  
  local http_code
  http_code=$(curl -s -w "%{http_code}" -o "$spec_file" \
    -H "Authorization: Bearer $TOKEN" \
    "$BASE_URL/services/$svc/v3/api-docs")
  
  if [ "$http_code" != "200" ]; then
    echo "✖ HTTP $http_code for $svc. Skipping."
    rm -f "$spec_file"
    return 1
  fi
  echo "✔ Spec for $svc downloaded."
  return 0
}

# 3) Merge all downloaded OpenAPI specs into a single file
merge_openapi_specs() {
  local merged_spec="openapi_spec_all.json"
  echo "Merging all OpenAPI specs into $merged_spec..."

  # Find all individual spec files
  local specs_to_merge=()
  for svc in "${SERVICE_NAMES[@]}"; do
    if [ -f "openapi_spec_${svc}.json" ]; then
      specs_to_merge+=("openapi_spec_${svc}.json")
    fi
  done

  if [ ${#specs_to_merge[@]} -eq 0 ]; then
    echo "✖ No spec files found to merge. Aborting."
    exit 1
  fi

  # Use the first spec as the base, then merge others into it
  cp "${specs_to_merge[0]}" "$merged_spec"
  
  # Deeply merge paths and components from the rest of the specs
  for (( i=1; i<${#specs_to_merge[@]}; i++ )); do
    jq -s '
      .[0] as $base | .[1] as $new | $base
      | .paths += ($new.paths // {})
      | .components.schemas += ($new.components.schemas // {})
      | .components.responses += ($new.components.responses // {})
      | .components.parameters += ($new.components.parameters // {})
      | .components.requestBodies += ($new.components.requestBodies // {})
      | .components.securitySchemes += ($new.components.securitySchemes // {})
    ' "$merged_spec" "${specs_to_merge[$i]}" > tmp_merged.json && mv tmp_merged.json "$merged_spec"
  done

  echo "✔ Merged OpenAPI spec saved to $merged_spec"
}

# 4) Generate UNIFIED TypeScript Axios client
generate_ts_client() {
  echo "Generating unified TypeScript Axios client in $TS_CLIENT_OUT_DIR..."
  openapi-generator-cli generate \
    -i "openapi_spec_all.json" \
    -g typescript-axios \
    -o "$TS_CLIENT_OUT_DIR" \
    --skip-validate-spec \
    --additional-properties=supportsES6=true,withSeparateModelsAndApi=true,apiPackage=api,modelPackage=model
  echo "✔ TypeScript client generated."
}

# 5) Generate UNIFIED Java Feign client
generate_java_feign_client() {
  echo "Generating unified Java Feign client in $JAVA_CLIENT_OUT_DIR..."
  openapi-generator-cli generate \
    -i "openapi_spec_all.json" \
    -g "java" \
    -o "$JAVA_CLIENT_OUT_DIR" \
    --library "feign" \
    --skip-validate-spec \
    --api-package "$JAVA_API_PACKAGE" \
    --model-package "$JAVA_MODEL_PACKAGE" \
    --additional-properties="interfaceOnly=true,dateLibrary=java8,useJakartaEe=false" # Set useJakartaEe=true for Spring Boot 3+
  echo "✔ Java Feign client generated."
}

# --- Main Execution ---
main() {
  echo "▶️ Starting API client generation script..."
  check_dependencies
  get_token

  # Create output directory for Postman collections
  mkdir -p "$POSTMAN_COLLECTIONS_DIR"

  # --- Part 1: Process each service individually ---
  for svc in "${SERVICE_NAMES[@]}"; do
    echo "--- Processing service: $svc ---"
    if ! fetch_openapi_spec "$svc"; then
      continue # Skip to the next service if fetch fails
    fi

    # Generate a temporary Postman collection for the service
    echo "Generating Postman collection for $svc..."
    local temp_postman_dir="./temp_postman/$svc"
    openapi-generator-cli generate \
      -i "openapi_spec_${svc}.json" \
      -g postman-collection \
      -o "$temp_postman_dir" \
      --skip-validate-spec

    local col_file="$temp_postman_dir/postman.json"
    
    # Process the collection with Node.js helper scripts if they exist
    [ -f "addAuthorizationHeader.js" ] && node addAuthorizationHeader.js "$col_file"
    [ -f "groupByClass.js" ] && node groupByClass.js "$col_file"
    
    # Patch schema URL
    jq '.info.schema = "https://schema.postman.com/json/collection/v2.1.0/collection.json"' "$col_file" > tmp.json && mv tmp.json "$col_file"

    # Move final collection to the destination
    mv "$col_file" "$POSTMAN_COLLECTIONS_DIR/${svc}.postman.json"
    echo "✔ Postman collection for $svc created at $POSTMAN_COLLECTIONS_DIR/${svc}.postman.json"
  done
  
  # --- Part 2: Merge specs and generate unified clients ---
  echo "--- Generating unified clients ---"
  merge_openapi_specs
  generate_ts_client
  generate_java_feign_client

  # Run final rewrite script if it exists
  [ -f "rewriteUrls.js" ] && node rewriteUrls.js

  # --- Part 3: Cleanup ---
  echo "--- Cleaning up intermediate files ---"
  rm -rf ./temp_postman
  rm -f openapi_spec_*.json
  # Keep the merged spec for inspection, or uncomment below to delete
  # rm -f openapi_spec_all.json

  echo "✅ Script finished successfully!"
}

# Run the main function
main
