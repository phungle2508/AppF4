#!/bin/bash

# Set the base URL for your Swagger API docs
BASE_URL="${BASE_URL:-https://appf4s.io.vn}"

# Keycloak credentials and endpoint
KEYCLOAK_URL="https://keycloak.appf4s.io.vn/realms/jhipster/protocol/openid-connect/token"
CLIENT_ID="web_app"
CLIENT_SECRET="your_client_secret_here"  # Update if needed
USERNAME="admin"
PASSWORD="admin"

# Output directory for Postman collections
OUTPUT_DIR="postman-collections"

# Curl timeout and retries
CURL_TIMEOUT=30
MAX_RETRIES=3

# Services to generate collections for
declare -a service_names=(  "reel")

# 1) Obtain Keycloak token
token_response=""
get_token() {
  echo "Getting Keycloak token..."
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

# 2) Fetch and validate OpenAPI spec
fetch_openapi_spec() {
  local svc="$1" spec="openapi_spec_${svc}.json"
  echo "Fetching spec for $svc..."
  http_code=$(curl -s -w "%{http_code}" -o "$spec" \
    -H "Authorization: Bearer $TOKEN" \
    "$BASE_URL/services/$svc/v3/api-docs")
  if [ "$http_code" != "200" ]; then
    echo "✖ HTTP $http_code for $svc. Skipping."
    rm -f "$spec"
    return 1
  fi
  echo "✔ Spec for $svc downloaded."
}

# 3) Generate Postman collection grouped by tags
generate_postman_collection() {
  local svc="$1" spec="openapi_spec_${svc}.json"
  echo "Generating Postman collection for $svc (useTags + Tags strategy)..."
  openapi-generator-cli generate \
    -i "$spec" \
    -g postman-collection \
    -o "$OUTPUT_DIR/$svc" \
    --skip-validate-spec \
    --additional-properties=useTags=true,folderStrategy=Tags
}
# 3a) Generate TypeScript Axios client + DTOs for Next.js
generate_ts_client() {
  local svc="$1" spec="openapi_spec_${svc}.json"
  local out_dir="generated-ts/$svc"
  echo "Generating TypeScript Axios client for $svc in $out_dir..."

  openapi-generator-cli generate \
    -i "$spec" \
    -g typescript-axios \
    -o "$out_dir" \
    --skip-validate-spec \
    --additional-properties=supportsES6=true,withSeparateModelsAndApi=true,apiPackage=api,modelPackage=model


  echo "✔ TypeScript client generated for $svc."
}

# 4) Inject Auth header via external Node script
add_authorization_header() {
  local col="$1"
  [ -f "$col" ] && node addAuthorizationHeader.js "$col"
}

# Create Postman environment with Keycloak login to set the token
create_keycloak_login_request() {
  local collection_file="$1"
  echo "Creating Keycloak login request to set the token in Postman environment..."

  # Create a POST method to Keycloak for login
  cat <<EOF > "$collection_file"
{
  "info": {
    "name": "Login API",
    "description": "Login API documentation",
    "version": "0.0.1",
    "schema": "https://schema.postman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Keycloak Login",
      "request": {
        "url": "{{keycloak_url}}/realms/jhipster/protocol/openid-connect/token",
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/x-www-form-urlencoded"
          }
        ],
        "body": {
          "mode": "urlencoded",
          "urlencoded": [
            { "key": "client_id", "value": "web_app" },
            { "key": "client_secret", "value": "$CLIENT_SECRET" },
            { "key": "username", "value": "$USERNAME" },
            { "key": "password", "value": "$PASSWORD" },
            { "key": "grant_type", "value": "password" }
          ]
        },
        "description": "Logs in to Keycloak and stores the access token"
      },
      "response": [],
      "event": [
        {
          "listen": "test",
          "script": {
            "exec": [
              "// Extract the access token and save it as an environment variable",
              "pm.environment.set('access_token', pm.response.json().access_token);",
              "// Optional: log token for debugging purposes",
              "console.log('Access Token:', pm.environment.get('access_token'));"
            ]
          }
        }
      ]
    }
  ]
}
EOF
  echo "Keycloak login request added to collection."
}

# 5) Flatten folder-per-path into one-folder-per-resource
flatten_by_class() {
  local col="$1"
  echo "Flattening $col into one folder per resource..."
  node groupByClass.js "$col"
}

# 6) Patch info.schema for UI import
patch_schema_url() {
  local col="$1"
  echo "Patching schema URL in $col..."
  jq '.info.schema = "https://schema.postman.com/json/collection/v2.1.0/collection.json"' "$col" > tmp && mv tmp "$col"
}

# 7) Create Postman Environment JSON with dynamic ms_servicename variables
create_postman_environment() {
  echo "Creating Postman environment with dynamic service variables..."

  ENV_JSON="{\"id\": \"1\", \"name\": \"AppF4 Environment\", \"values\": ["
  
  # Add the baseURL variable
  ENV_JSON+="{\"key\": \"baseURL\", \"value\": \"$BASE_URL\", \"enabled\": true},"

  # Add ms_servicename for each service
  for svc in "${service_names[@]}"; do
    ENV_JSON+="{\"key\": \"$svc\", \"value\": \"services/$svc\", \"enabled\": true},"
  done

  # Add the keycloak_url
  ENV_JSON+="{\"key\": \"keycloak_url\", \"value\": \"https://keycloak.appf4s.io.vn\", \"enabled\": true}"

  ENV_JSON+="]}"

  # Save to the file
  echo $ENV_JSON > postman_environment.json
  echo "Postman environment created: postman_environment.json"
}

# Main flow
echo "▶️ Starting generation..."
get_token
mkdir -p "$OUTPUT_DIR"
create_keycloak_login_request "$OUTPUT_DIR/keycloak_login.postman.json"
mkdir -p ./all_collections  # Ensure the folder exists
mv "$OUTPUT_DIR/keycloak_login.postman.json" "./all_collections/keycloak_login.postman.json"

# Generate Postman environment
create_postman_environment

for svc in "${service_names[@]}"; do
  fetch_openapi_spec "$svc" || continue
  generate_postman_collection "$svc"
  generate_ts_client "$svc"
  col_file="$OUTPUT_DIR/$svc/postman.json"
  add_authorization_header "$col_file"
  flatten_by_class "$col_file"
  patch_schema_url "$col_file"
  
  # Move each service's collection to root as <service>.postman.json
  echo "Moving $col_file -> ./all_collections/${svc}.postman.json"

  mv "$col_file" "./all_collections/${svc}.postman.json"
done

# Run the JavaScript file to update the URLs and variables
node rewriteUrls.js

# Cleanup intermediate files and folders
rm -rf "$OUTPUT_DIR"
rm -f openapi_spec_*.json

# Final message
echo "✅ Done. You now have only JavaScript helpers and .postman.json files in this directory."
