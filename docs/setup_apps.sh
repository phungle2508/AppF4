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

# Function to add dependency if not exists
add_dependency_if_not_exists() {
    local pom_file="$1"
    if [ -f "$pom_file" ]; then
        if ! grep -q "com.jcraft" "$pom_file"; then
            # Find the dependencies section and add our new dependency
            sed -i '/<dependencies>/a \
        <dependency>\
            <groupId>com.jcraft</groupId>\
            <artifactId>jsch</artifactId>\
            <version>0.1.55</version>\
        </dependency>' "$pom_file"
            echo -e "${GREEN}Added JSch dependency to $pom_file${NC}"
        fi
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
            sed -i "s|jdbc:mysql://localhost:[0-9][0-9]*/[^<]*|jdbc:mysql://microservices.appf4.io.vn:$mysql_port/$clean_name|g" "$pom_file"
            
            # For liquibase-plugin.url tags specifically
            sed -i "s|<liquibase-plugin.url>jdbc:mysql://localhost:[0-9][0-9]*/[^<]*</liquibase-plugin.url>|<liquibase-plugin.url>jdbc:mysql://microservices.appf4.io.vn:$mysql_port/$clean_name</liquibase-plugin.url>|g" "$pom_file"
            
            echo -e "${GREEN}Updated MySQL connection in $pom_file to jdbc:mysql://microservices.appf4.io.vn:$mysql_port/$clean_name${NC}"
        fi
        
        # Also update any existing URLs that might have the wrong database name
        if grep -q "jdbc:mysql://microservices.appf4.io.vn" "$pom_file"; then
            # For regular JDBC URLs in properties
            sed -i "s|jdbc:mysql://microservices.appf4.io.vn:[0-9][0-9]*/[^<]*|jdbc:mysql://microservices.appf4.io.vn:$mysql_port/$clean_name|g" "$pom_file"
            
            # For liquibase-plugin.url tags specifically
            sed -i "s|<liquibase-plugin.url>jdbc:mysql://microservices.appf4.io.vn:[0-9][0-9]*/[^<]*</liquibase-plugin.url>|<liquibase-plugin.url>jdbc:mysql://microservices.appf4.io.vn:$mysql_port/$clean_name</liquibase-plugin.url>|g" "$pom_file"
            
            echo -e "${GREEN}Fixed existing MySQL connection in $pom_file to use database name $clean_name${NC}"
        fi
    fi
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
    add_dependency_if_not_exists "../backend/$app/pom.xml"
    
    # Update MySQL connections in configuration files
    if [ "$app" != "gateway" ]; then
        update_mysql_connections "$app"
    fi
    
    echo -e "${GREEN}Completed setup for $app${NC}"
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
done

echo -e "\n${GREEN}Script completed!${NC}"