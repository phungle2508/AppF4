#!/bin/bash

# Set the base URL for your Swagger API docs
BASE_URL="${BASE_URL:-https://appf4.io.vn}"

# Set the Keycloak credentials and endpoint
KEYCLOAK_URL="https://keycloak.appf4.io.vn/realms/jhipster/protocol/openid-connect/token"
CLIENT_ID="web_app"
CLIENT_SECRET="your_client_secret_here"  # Add your client secret here if needed
USERNAME="admin"
PASSWORD="admin"

# Set the output directory for Postman collections
OUTPUT_DIR="postman-collections"

# Maximum number of retry attempts
MAX_RETRIES=3
# Timeout for curl requests in seconds
CURL_TIMEOUT=30

# List of service names (without paths) to generate Postman collections for
declare -a service_names=(
  "msuser"
  "msreels"
  "mscommentlike"
  "msnotification"
  "msfeed"
)

# Obtain the Keycloak token using OAuth2
get_token() {
  echo "Getting Keycloak token..."
  TOKEN_RESPONSE=$(curl -s -X POST "$KEYCLOAK_URL" \
    -d "client_id=$CLIENT_ID" \
    -d "client_secret=$CLIENT_SECRET" \
    -d "username=$USERNAME" \
    -d "password=$PASSWORD" \
    -d "grant_type=password")

  # Extract token from response
  TOKEN=$(echo "$TOKEN_RESPONSE" | grep -o '"access_token":"[^"]*"' | cut -d'"' -f4)

  # Check if we got a token
  if [ -z "$TOKEN" ]; then
    echo "Error: Unable to get Keycloak token."
    echo "Response from Keycloak:"
    echo "$TOKEN_RESPONSE"
    exit 1
  fi

  echo "Keycloak token obtained successfully."
}

# Function to fetch OpenAPI spec and check its validity
fetch_openapi_spec() {
  local service_name="$1"
  local openapi_spec_file="openapi_spec_$service_name.json"
  
  echo "Fetching OpenAPI spec for $service_name..."

  # Fetch OpenAPI spec using the obtained OAuth2 token
  response=$(curl -s -w "%{http_code}" -o "$openapi_spec_file" -H "Authorization: Bearer $TOKEN" "$BASE_URL/services/$service_name/v3/api-docs")
  
  # Check if the response code is 200 (Success)
  if [[ "$response" != "200" ]]; then
    echo "Error: Received an error response ($response) for $service_name. Skipping."
    rm "$openapi_spec_file"
    return 1
  fi

  # Check if the content returned is JSON (not HTML)
  content_type=$(curl -s -I -H "Authorization: Bearer $TOKEN" "$BASE_URL/services/$service_name/v3/api-docs" | grep -i "Content-Type" | awk '{print $2}' | tr -d '\r')

  if [[ "$content_type" != *"application/json"* ]]; then
    echo "Error: The OpenAPI spec for $service_name is not valid JSON. Skipping."
    rm "$openapi_spec_file"
    return 1
  fi

  echo "OpenAPI spec for $service_name fetched successfully."
  return 0
}

# Regenerate Postman collection using openapi-generator-cli
generate_postman_collection() {
  local service_name="$1"
  local openapi_spec_file="openapi_spec_$service_name.json"
  
  # Generate Postman collection with --skip-validate-spec to bypass validation errors
  echo "Generating Postman collection for $service_name..."
  openapi-generator-cli generate -i "$openapi_spec_file" -g postman-collection -o "$OUTPUT_DIR/$service_name" --skip-validate-spec
}

# Modify Postman Collection and add Authorization header using Node.js script
add_authorization_header() {
  local collection_file="$1"

  # Ensure the postman collection file exists
  if [ -f "$collection_file" ]; then
    echo "Postman collection created successfully at $collection_file."
    # Add Authorization header to all requests in the Postman collection using Node.js
    node addAuthorizationHeader.js "$collection_file" 
  else
    echo "Error: $collection_file does not exist."
  fi
}

# Main Script Execution

# Obtain the Keycloak token
get_token

# Create output directory if it doesn't exist
mkdir -p "$OUTPUT_DIR"

# Loop through services and generate OpenAPI specs and Postman collections
for service_name in "${service_names[@]}"; do
  # Fetch the OpenAPI spec using the new function
  fetch_openapi_spec "$service_name"
  
  if [ $? -ne 0 ]; then
    echo "Skipping $service_name due to invalid OpenAPI spec or non-JSON response."
    continue
  fi

  # Define the OpenAPI spec file path
  OPENAPI_SPEC_FILE="openapi_spec_$service_name.json"

  # If the OpenAPI spec is generated, proceed to generate the Postman collection
  generate_postman_collection "$service_name"

  # Define the Postman collection file path
  COLLECTION_FILE="$OUTPUT_DIR/$service_name/postman.json"

  # Modify the Postman collection and add the Authorization header
  add_authorization_header "$COLLECTION_FILE"
done

echo "Process completed. Check $OUTPUT_DIR for collections."
