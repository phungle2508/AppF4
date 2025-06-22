#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to create directory if it doesn't exist
create_dir_if_not_exists() {
    if [ ! -d "$1" ]; then
        mkdir -p "$1"
        echo -e "${BLUE}Created directory: $1${NC}"
    fi
}

# Function to copy file with logging
copy_file() {
    local src="$1"
    local dest="$2"
    if [ -f "$src" ]; then
        cp "$src" "$dest"
        echo -e "${GREEN}Copied: $src -> $dest${NC}"
    else
        echo "Warning: Source file not found: $src"
    fi
}

# Function to get clean app name (remove ms_ prefix)
get_clean_name() {
    local app_name="$1"
    echo "${app_name#ms_}"
}
# Function to remove broker directory from microservice

# Function to update package name in Java file
update_package() {
    local file="$1"
    local app_name="$2"
    local clean_name=$(get_clean_name "$app_name")
    if [ -f "$file" ]; then
        # Update package declaration
        sed -i "s/package com\.f4\.gateway/package com.f4.${clean_name}/" "$file"
        # Update imports
        sed -i "s/com\.mycompany\.myapp\.config/com.f4.${clean_name}.config/" "$file"
        # Update logger class reference
        sed -i "s/LoggerFactory\.getLogger(Main\.class)/LoggerFactory.getLogger(Dev${clean_name^}.class)/" "$file"
        # Update constructor name
        sed -i "s/public Main(/public Dev${clean_name^}(/" "$file"
        # Update SpringApplication references
        sed -i "s/SpringApplication(Main\.class)/SpringApplication(Dev${clean_name^}.class)/" "$file"
        echo -e "${GREEN}Updated package names and class references for ${clean_name}${NC}"
    fi
}
# Function to copy Avro schema files
# Function to copy Avro schema and utility files
copy_avro_files() {
    local service="$1"
    local clean_name=$(get_clean_name "$service")
    local avro_schema_src="template/microservice/avro/${clean_name}/event-envelope.avsc"
    local avro_schema_dest="../backend/$service/src/main/resources/avro"
    local avro_util_src="template/microservice/avro/${service}/AvroConverter.java"
    local avro_util_dest="../backend/$service/src/main/java/com/f4/${clean_name}/kafka/util"
    local kafka_util_src="template/microservice/avro/${service}/KafkaUtilityService.java"
    local kafka_util_dest="../backend/$service/src/main/java/com/f4/${clean_name}/kafka/service"
    local kafka_job_runner_src="template/microservice/avro/${service}/KafkaJobRunner.java"
    local kafka_job_runner_dest="../backend/$service/src/main/java/com/f4/${clean_name}/kafka/runner"
    local post_reel_handler_src="template/microservice/avro/${service}/PostReelHandler.java"
    local post_reel_handler_dest="../backend/$service/src/main/java/com/f4/${clean_name}/kafka/handler/events"

    # Copy Avro schema file
    if [ -f "$avro_schema_src" ]; then
        create_dir_if_not_exists "$avro_schema_dest"
        copy_file "$avro_schema_src" "$avro_schema_dest/event-envelope.avsc"
        echo -e "${GREEN}Copied Avro schema for ${clean_name}${NC}"
    else
        echo "Warning: Avro schema not found: $avro_schema_src"
    fi
    
    # Copy and rename AvroConverter to AvroConverter
    if [ -f "$avro_util_src" ]; then
        create_dir_if_not_exists "$avro_util_dest"
        copy_file "$avro_util_src" "$avro_util_dest/AvroConverter.java"
        
        
        echo -e "${GREEN}Copied and renamed AvroConverter  for ${clean_name}${NC}"
    else
        echo "Warning: AvroConverter not found: $avro_util_src"
    fi

      # Copy and rename KafkaUtilityService to KafkaUtilityService
    if [ -f "$kafka_util_src" ]; then
        create_dir_if_not_exists "$kafka_util_dest"
        copy_file "$kafka_util_src" "$kafka_util_dest/KafkaUtilityService.java"
        
        
        echo -e "${GREEN}Copied and renamed KafkaUtilityService  for ${clean_name}${NC}"
    else
        echo "Warning: KafkaUtilityService not found: $kafka_util_src"
    fi
       # Copy KafkaJobRunner
    if [ -f "$kafka_job_runner_src" ]; then
        create_dir_if_not_exists "$kafka_job_runner_dest"
        copy_file "$kafka_job_runner_src" "$kafka_job_runner_dest/KafkaJobRunner.java"
        
        # Update package in the copied file
        sed -i "s/package com\.f4\.reel/package com.f4.${clean_name}/" "$kafka_job_runner_dest/KafkaJobRunner.java"
        
        echo -e "${GREEN}Copied KafkaJobRunner for ${clean_name}${NC}"
    else
        echo "Warning: KafkaJobRunner not found: $kafka_job_runner_src"
    fi

    # Copy PostReelHandler
    if [ -f "$post_reel_handler_src" ]; then
        create_dir_if_not_exists "$post_reel_handler_dest"
        copy_file "$post_reel_handler_src" "$post_reel_handler_dest/PostReelHandler.java"
        
        # Update package and class name in the copied file
        sed -i "s/package com\.f4\.reel/package com.f4.${clean_name}/" "$post_reel_handler_dest/PostReelHandler.java"
        sed -i "s/PostReelHandler/Post${clean_name^}Handler/g" "$post_reel_handler_dest/PostReelHandler.java"
        
        # Rename the file to match the service
        mv "$post_reel_handler_dest/PostReelHandler.java" "$post_reel_handler_dest/Post${clean_name^}Handler.java"
        
        echo -e "${GREEN}Copied and renamed PostReelHandler to Post${clean_name^}Handler for ${clean_name}${NC}"
    else
        echo "Warning: PostReelHandler not found: $post_reel_handler_src"
    fi
}
add_dependency_if_not_exists() {
    local pom_file="$1"
    local ms_name="$2"  # microservice name, e.g., "commentlike"
    local template_pom="template/microservice/pom.xml"
    echo "Adding dependency to $pom_file for microservice '$ms_name'"
    # If ms_name is user or gateway, skip the entire copy + replacement block
    if [[ "$ms_name" == "user" || "$ms_name" == "gateway" ]]; then
        echo "ℹ️ Skipping template copy and replacement for '$ms_name'"
    else
        if [ ! -f "$template_pom" ]; then
            echo "❌ Template file $template_pom not found."
            return 1
        fi

        if [ ! -f "$pom_file" ]; then
            echo "❌ Target file $pom_file not found."
            return 1
        fi

        # Capitalize first letter for replacing "Reel"
        local ms_name_cap="$(tr '[:lower:]' '[:upper:]' <<< ${ms_name:0:1})${ms_name:1}"

        # Copy template to target pom.xml
        cp "$template_pom" "$pom_file"
        echo "✅ Replaced $pom_file with $template_pom"

        # Replace lowercase "reel" with microservice name
        sed -i "s/reel/${ms_name}/g" "$pom_file"
        # Replace capitalized "Reel" with capitalized microservice name
        sed -i "s/Reel/${ms_name_cap}/g" "$pom_file"

        echo "✅ Replaced 'reel' with '${ms_name}' and 'Reel' with '${ms_name_cap}' in $pom_file"
    fi

    # For all ms_names except user and gateway, add JSch dependency if not already present
    if ! grep -q "com.jcraft" "$pom_file"; then
            sed -i '/<dependencies>/a \
                    <dependency>\
                        <groupId>com.jcraft</groupId>\
                        <artifactId>jsch</artifactId>\
                        <version>0.1.55</version>\
                    </dependency>' "$pom_file"
            echo "✅ Added JSch dependency to $pom_file"
    else
            echo "ℹ️ JSch dependency already present in $pom_file, skipping insertion."
    fi
}


# Function to get MySQL port from server port
get_mysql_port() {
    local app_name="$1"
    case "$app_name" in
        "user")
            echo "3381"  # 8081 -> 3381
            ;;
        "reel")
            echo "3382"  # 8082 -> 3382
            ;;
        "notification")
            echo "3383"  # 8083 -> 3383
            ;;
        "feed")
            echo "3384"  # 8084 -> 3384
            ;;
        "commentlike")
            echo "3385"  # 8085 -> 3385
            ;;
        *)
            echo "3306"  # Default MySQL port
            ;;
    esac
}

# Function to update consul config
update_consul_config() {
    local file="$1"
    local app_name="$2"
    if [ -f "$file" ]; then
        # Update service name and database name
        sed -i "s/service-name:.*/service-name: ${app_name}/" "$file"
        sed -i "s/mysql_database:.*/mysql_database: ${app_name}/" "$file"
        
        # Get MySQL port based on app name
        local mysql_port=$(get_mysql_port "$app_name")
        
        # Update MySQL port
        sed -i "s/mysql_port:.*/mysql_port: ${mysql_port}/" "$file"
        
        # Update jdbc URL to use microservices.appf4.io.vn with correct database name
        sed -i "s|jdbc:mysql://localhost:[0-9][0-9]*/[^/]*|jdbc:mysql://microservices.appf4.io.vn:${mysql_port}/${app_name}|g" "$file"
        
        echo -e "${GREEN}Updated consul config for ${app_name} with MySQL port ${mysql_port} and host microservices.appf4.io.vn${NC}"
    fi
}

# Function to update MySQL connections in config files
update_mysql_connections() {
    local service="$1"
    local clean_name=$(get_clean_name "$service")
    local mysql_port=$(get_mysql_port "$clean_name")
    
    # Look for application-*.yml files
    for config_file in $(find "../backend/$service" -name "application*.yml" -type f); do
        echo -e "Updating MySQL connection in: $config_file"
        
        # Replace jdbc URL in the file with correct database name
        if grep -q "jdbc:mysql://localhost:[0-9]" "$config_file"; then
            sed -i "s|jdbc:mysql://localhost:[0-9][0-9]*/[^/]*|jdbc:mysql://microservices.appf4.io.vn:$mysql_port/$clean_name|g" "$config_file"
            echo -e "${GREEN}Updated MySQL connection in $config_file to jdbc:mysql://microservices.appf4.io.vn:$mysql_port/$clean_name${NC}"
        fi
        
        # Also update any existing URLs that might have the wrong database name
        if grep -q "jdbc:mysql://microservices.appf4.io.vn" "$config_file"; then
            sed -i "s|jdbc:mysql://microservices.appf4.io.vn:[0-9][0-9]*/[^/]*|jdbc:mysql://microservices.appf4.io.vn:$mysql_port/$clean_name|g" "$config_file"
            echo -e "${GREEN}Fixed existing MySQL connection in $config_file to use database name $clean_name${NC}"
        fi
    done
    
    # Look for pom.xml file
    local pom_file="../backend/$service/pom.xml"
    if [ -f "$pom_file" ]; then
        echo -e "Updating MySQL connection in: $pom_file"
        
        # Replace jdbc URL in the file with correct database name - more careful with XML tags
        if grep -q "jdbc:mysql://localhost:[0-9]" "$pom_file"; then
            # For regular JDBC URLs in properties
            sed -i "s|jdbc:mysql://localhost:[0-9][0-9]*/[^<]*|jdbc:mysql://microservices.appf4.io.vn:$mysql_port/$clean_name?allowLoadLocalInfile=true|g" "$pom_file"
            
            # For liquibase-plugin.url tags specifically
            sed -i "s|<liquibase-plugin.url>jdbc:mysql://localhost:[0-9][0-9]*/[^<]*</liquibase-plugin.url>|<liquibase-plugin.url>jdbc:mysql://microservices.appf4.io.vn:$mysql_port/$clean_name?allowLoadLocalInfile=true</liquibase-plugin.url>|g" "$pom_file"
            
            echo -e "${GREEN}Updated MySQL connection in $pom_file to jdbc:mysql://microservices.appf4.io.vn:$mysql_port/$clean_name?allowLoadLocalInfile=true${NC}"
        fi
        
        # Also update any existing URLs that might have the wrong database name
        if grep -q "jdbc:mysql://microservices.appf4.io.vn" "$pom_file"; then
            # For regular JDBC URLs in properties
            sed -i "s|jdbc:mysql://microservices.appf4.io.vn:[0-9][0-9]*/[^<]*|jdbc:mysql://microservices.appf4.io.vn:$mysql_port/$clean_name?allowLoadLocalInfile=true|g" "$pom_file"
            
            # For liquibase-plugin.url tags specifically
            sed -i "s|<liquibase-plugin.url>jdbc:mysql://microservices.appf4.io.vn:[0-9][0-9]*/[^<]*</liquibase-plugin.url>|<liquibase-plugin.url>jdbc:mysql://microservices.appf4.io.vn:$mysql_port/$clean_name?allowLoadLocalInfile=true</liquibase-plugin.url>|g" "$pom_file"
            
            echo -e "${GREEN}Fixed existing MySQL connection in $pom_file to use database name $clean_name?allowLoadLocalInfile=true${NC}"
        fi
    fi
}

# Function to recursively replace com.f4.reel with com.f4.{serviceName} in all Java files in a directory
global_replace_reel_package() {
    local src_dir="$1"
    local service_name="$2"
    find "$src_dir" -type f -name "*.java" | while read -r java_file; do
        sed -i "s/com\\.f4\\.reel/com.f4.${service_name}/g" "$java_file"
        # echo -e "${GREEN}Replaced package in $java_file${NC}"
    done
}

# Function to copy and overwrite a file, creating the destination directory if needed
overwrite_file() {
    local src="$1"
    local dest="$2"
    mkdir -p "$(dirname "$dest")"
    cp -f "$src" "$dest"
    echo -e "${GREEN}Overwritten: $src -> $dest${NC}"
}

# Function to copy and overwrite a folder recursively, creating the destination directory if needed
overwrite_folder() {
    local src_dir="$1"
    local dest_dir="$2"
    mkdir -p "$dest_dir"
    cp -rf "$src_dir"/* "$dest_dir"/
    # echo -e "${GREEN}Overwritten folder: $src_dir -> $dest_dir${NC}"
}

# Function to update import com.f4.reel to import com.f4.{serviceName} in all Java files of a service
update_imports_in_service() {
    local service_dir="$1"
    local service_name="$2"
    find "$service_dir/src" -type f -name "*.java" | while read -r java_file; do
        sed -i "s/import com\\.f4\\.reel/import com.f4.${service_name}/g" "$java_file"
        # echo -e "${GREEN}Updated imports in $java_file${NC}"
    done
}

# Function to update package com.f4.reel.* to package com.f4.{serviceName}.* in all Java files of a service
update_package_declaration_in_service() {
    local service_dir="$1"
    local service_name="$2"
    find "$service_dir/src" -type f -name "*.java" | while read -r java_file; do
        sed -i "s/^package com\\.f4\\.reel\\./package com.f4.${service_name}./g" "$java_file"
        # echo -e "${GREEN}Updated package declaration in $java_file${NC}"
    done
}
# Function to update "ReelDTO" to "{ServiceName}DTO" in all Java files in a service
update_dto_name_in_service() {
    local service_dir="$1"  # This should be like "../backend/ms_commentlike"
    local service_name="$2"  # This should be like "Commentlike"
    
    echo -e "${BLUE}Updating DTO class names in $service_dir from ReelDTO to ${service_name}DTO${NC}"
    
    # Make sure the path is correct by using the full service directory
    find "$service_dir/src" -type f -name "*.java" | while read -r java_file; do
    # Check if file contains ReelDTO before attempting replacement
    # Inside your function or loop for each Java file:
        if [ "$service_name" = "Feed" ]; then
            if grep -q "ReelService" "$java_file"; then
                sed -i -E "s/(^|[^a-zA-Z0-9_])ReelService([^a-zA-Z0-9_]|$)/\1FeedItemService\2/g" "$java_file"
                echo -e "${GREEN}Updated ReelService to FeedService in $java_file${NC}"
            fi
            if grep -q "ReelDTO" "$java_file"; then
                sed -i -E "s/(^|[^a-zA-Z0-9_])ReelDTO([^a-zA-Z0-9_]|$)/\1FeedItemDTO\2/g" "$java_file"
                echo -e "${GREEN}Updated ReelDTO to FeedDTO in $java_file${NC}"
            fi
        else
            # For other services, you can do the simple replace
            if grep -q "ReelService" "$java_file"; then
                sed -i "s/ReelService/${service_name}Service/g" "$java_file"
                echo -e "${GREEN}Updated ReelService to ${service_name}Service in $java_file${NC}"
            fi
            if grep -q "ReelDTO" "$java_file"; then
                sed -i "s/ReelDTO/${service_name}DTO/g" "$java_file"
                echo -e "${GREEN}Updated ReelDTO to ${service_name}DTO in $java_file${NC}"
            fi
        fi

    done
}

# Main script
echo "Starting to copy template files to all apps..."

# List of all apps in backend folder
apps=(
    "ms_user"
    "ms_commentlike"
    "ms_reel"
    "ms_feed"
    "ms_notification"
    "gateway"
)

# Process each app
for app in "${apps[@]}"; do
    echo -e "\n${BLUE}Processing $app...${NC}"
    clean_name=$(get_clean_name "$app")
    rm -rf "../backend/$app/src/main/java/com/f4/${clean_name}/broker"
    # Create necessary directories
    create_dir_if_not_exists "../backend/$app/src/main/java/com/f4/${clean_name}"
    create_dir_if_not_exists "../backend/$app/src/main/resources/config/tls"
    

    # Copy Main.java, rename it, and update package
    copy_file "template/Main.java" "../backend/$app/src/main/java/com/f4/${clean_name}/Dev${clean_name^}.java"
    update_package "../backend/$app/src/main/java/com/f4/${clean_name}/Dev${clean_name^}.java" "${app}"
    
    # Update class name in the Java file
    if [ -f "../backend/$app/src/main/java/com/f4/${clean_name}/Dev${clean_name^}.java" ]; then
        sed -i "s/public class Main/public class Dev${clean_name^}/" "../backend/$app/src/main/java/com/f4/${clean_name}/Dev${clean_name^}.java"
        echo -e "${GREEN}Updated class name to Dev${clean_name^}${NC}"
    fi
    
    # Copy TLS files
    copy_file "template/tls/kafka.truststore.jks" "../backend/$app/src/main/resources/config/tls/kafka.truststore.jks"
    copy_file "template/tls/kafka.keystore.jks" "../backend/$app/src/main/resources/config/tls/kafka.keystore.jks"
    
    # Copy and update consul config based on app type
    if [ "$app" = "gateway" ]; then
        copy_file "template/gateway/consul-config-dev.yml" "../backend/$app/src/main/resources/config/consul-config-dev.yml"
    else
        copy_file "template/microservice/consul-config-dev.yml" "../backend/$app/src/main/resources/config/consul-config-dev.yml"
    fi
    update_consul_config "../backend/$app/src/main/resources/config/consul-config-dev.yml" "${clean_name}"
    
    # Add dependency to pom.xml
    add_dependency_if_not_exists "../backend/$app/pom.xml"  "$clean_name"
    
    # Update MySQL connections in configuration files
    if [ "$app" != "gateway" ]; then
        update_mysql_connections "$app"
    fi
    
    echo -e "${GREEN}Completed setup for $app${NC}"
done

# --- Custom logic for ms_reel with temp dir for safe replacement ---
for app in "${apps[@]}"; do
    clean_name=$(get_clean_name "$app")
    if [ "$app" = "ms_reel" ]; then
        temp_dir="/tmp/ms_reel_template_$$"
        rm -rf "$temp_dir"
        mkdir -p "$temp_dir"
        cp -r template/microservice/* "$temp_dir"/
        # Replace all com.f4.reel with com.f4.reel (for ms_reel) in temp_dir before copying
        find "$temp_dir" -type f -name "*.java" | while read -r java_file; do
            sed -i "s/com\\.f4\\.reel/com.f4.${clean_name}/g" "$java_file"
            # echo -e "${GREEN}Replaced package in $java_file${NC}"
        done
        # Overwrite MsReelKafkaResource.java
        overwrite_file "$temp_dir/MsReelKafkaResource.java" "../backend/$app/src/main/java/com/f4/${clean_name}/web/rest/MsReelKafkaResource.java"
        # Overwrite broker and handler folders
        overwrite_folder "$temp_dir/kafka" "../backend/$app/src/main/java/com/f4/${clean_name}/kafka"
        update_dto_name_in_service "/$ms" "$clean_name"
  
        rm -rf "$temp_dir"
    fi
done

# Apply template copying for all microservices except gateway and ms_user
for app in "${apps[@]}"; do
    clean_name=$(get_clean_name "$app")
    # Apply template copying for all microservices except gateway and ms_user
    if [ "$app" != "gateway" ] && [ "$app" != "ms_user" ]; then
        echo -e "${BLUE}Applying microservice templates to $app...${NC}"
        temp_dir="/tmp/${app}_template_$$"
        rm -rf "$temp_dir"
        mkdir -p "$temp_dir"
        cp -r template/microservice/* "$temp_dir"/
        # Replace all com.f4.reel with com.f4.${clean_name} in temp_dir before copying
        find "$temp_dir" -type f -name "*.java" | while read -r java_file; do
            sed -i "s/com\\.f4\\.reel/com.f4.${clean_name}/g" "$java_file"
            # echo -e "${GREEN}Replaced package in $java_file${NC}"
        done
        
        # Create resource filename based on service name
        resource_filename="Ms${clean_name^}KafkaResource.java"
        resource_orig_filename="MsReelKafkaResource.java"
        
    # Rename MsReelKafkaResource.java to appropriate service name if it exists
        if [ -f "$temp_dir/$resource_orig_filename" ]; then
            mv "$temp_dir/$resource_orig_filename" "$temp_dir/$resource_filename"
            # Update class name inside the file
            sed -i "s/MsReelKafkaResource/Ms${clean_name^}KafkaResource/g" "$temp_dir/$resource_filename"
            echo -e "${GREEN}Renamed and updated $resource_orig_filename to $resource_filename${NC}"
        fi
        

        # Overwrite KafkaResource file
        overwrite_file "$temp_dir/$resource_filename" "../backend/$app/src/main/java/com/f4/${clean_name}/web/rest/$resource_filename"
        
        # Overwrite broker and handler folders
        overwrite_folder "$temp_dir/kafka" "../backend/$app/src/main/java/com/f4/${clean_name}/kafka"
        # Capitalize first letter of service name for DTO
        capitalized_name="${clean_name^}"
        # Pass the complete path to the service directory
        update_dto_name_in_service "../backend/$app" "$capitalized_name"
        
        rm -rf "$temp_dir"
    fi
done

# Update imports for all ms_* services
declare -a ms_services=("ms_user" "ms_commentlike" "ms_reel" "ms_feed" "ms_notification")
for ms in "${ms_services[@]}"; do
    clean_name=$(get_clean_name "$ms")
    update_imports_in_service "../backend/$ms" "$clean_name"
done

# Update package declarations for all ms_* services
for ms in "${ms_services[@]}"; do
    clean_name=$(get_clean_name "$ms")
    update_package_declaration_in_service "../backend/$ms" "$clean_name"
done

echo -e "\n${GREEN}All applications have been updated with template files!${NC}"

# Verify the setup
echo -e "\n${BLUE}Verifying setup...${NC}"
for app in "${apps[@]}"; do
    clean_name=$(get_clean_name "$app")
    echo -e "\nChecking $app:"
    
    # Check Java file
    if [ -f "../backend/$app/src/main/java/com/f4/${clean_name}/Dev${clean_name^}.java" ]; then
        echo -e "${GREEN}✓ Dev${clean_name^}.java exists${NC}"
    else
        echo "✗ Dev${clean_name^}.java is missing"
    fi
    
    # Check TLS files
    if [ -f "../backend/$app/src/main/resources/config/tls/kafka.truststore.jks" ] && \
       [ -f "../backend/$app/src/main/resources/config/tls/kafka.keystore.jks" ]; then
        echo -e "${GREEN}✓ TLS files exist${NC}"
    else
        echo "✗ TLS files are missing"
    fi
    
    # Check consul config
    if [ -f "../backend/$app/src/main/resources/config/consul-config-dev.yml" ]; then
        echo -e "${GREEN}✓ consul-config-dev.yml exists${NC}"
    else
        echo "✗ consul-config-dev.yml is missing"
    fi
    copy_avro_files "$app"
done

echo -e "\n${GREEN}Script completed!${NC}"