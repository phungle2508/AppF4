const fs = require('fs');
const path = require('path');

// Directory containing the collection files
const collectionsDir = './all_collections/';

// Load Postman environment JSON file
const environmentFile = 'postman_environment.json';
const environment = JSON.parse(fs.readFileSync(environmentFile, 'utf8'));

// Extract the service-specific variables from the environment
const serviceVariables = environment.values.filter(item => item.key.startsWith('ms_'));
const baseUrl = environment.values.find(item => item.key === 'baseURL');

// Function to replace 'baseUrl' with '{{baseUrl}}/{{ms_serviceName}}' in the 'host' field
function updateHost(jsonData, ms_name) {
    function processItem(item) {
        if (item.item) {
            if (Array.isArray(item.item)) {
                item.item.forEach(processItem);
            } else if (item.item.item && Array.isArray(item.item.item)) {
                item.item.item.forEach(processItem);
            }
        }

        if (item.request && item.request.url) {
            // Log the original URL
            console.log(`Before Update - raw: ${item.request.url.raw}`);
            console.log(`Before Update - host: ${item.request.url.host}`);

            // Update the raw URL
            if (item.request.url.raw) {
                item.request.url.raw = item.request.url.raw.replace(
                    "{{baseUrl}}",
                    `{{baseURL}}/{{${ms_name}}}`
                );
            }

            // Update the host array
            if (item.request.url.host) {
                item.request.url.host = item.request.url.host.map(host => {
                    if (host === "{{baseUrl}}") {
                        return `{{baseURL}}/{{${ms_name}}}`;
                    }
                    return host;
                });
            }

            // Log the updated URL
            console.log(`After Update - raw: ${item.request.url.raw}`);
            console.log(`After Update - host: ${item.request.url.host}`);
        }
    }

    // Start processing from the root items
    if (jsonData.item && Array.isArray(jsonData.item)) {
        jsonData.item.forEach(processItem);
    }
}

// Function to update the variable section in the collection
function updateVariables(collection, ms_name) {
    const collectionVariables = collection.variable || [];

    // Remove the old baseUrl variable if it exists
    const baseUrlIndex = collectionVariables.findIndex(v => v.key === 'baseUrl');
    if (baseUrlIndex !== -1) {
        collectionVariables.splice(baseUrlIndex, 1);
    }

    // Ensure only the new baseURL variable is added
    const baseURLIndex = collectionVariables.findIndex(v => v.key === 'baseURL');
    if (baseURLIndex === -1) {
        collectionVariables.push({
            key: 'baseURL',
            value: '{{baseURL}}',  // The new baseURL format with dynamic service-specific variable
            type: 'string'
        });
    }

    // Add dynamic service-specific variable (e.g. {{ms_serviceName}})
    if (!collectionVariables.some(v => v.key === ms_name)) {
        collectionVariables.push({
            key: ms_name,
            value: `services/${ms_name}`,
            type: 'string'
        });
    }

    // Update the collection with the modified variables
    collection.variable = collectionVariables;
}

// Process each Postman collection file in the all_collections directory
fs.readdirSync(collectionsDir).forEach(file => {
    if (file.endsWith('.postman.json')) {
        const collectionFilePath = path.join(collectionsDir, file);

        try {
            // Read the collection file
            const collection = JSON.parse(fs.readFileSync(collectionFilePath, 'utf8'));

            // Check if 'item' is present in the collection
            if (collection.item) {
                // Get ms_name from the file name or other source (you can modify as per your needs)
                const ms_name = file.split('.')[0]; // Assuming ms_name is the file name without extension

                // Replace baseUrl with ms_serviceName in 'host' field and update the variables
                updateHost(collection, ms_name);
                updateVariables(collection, ms_name);

                // Save the updated collection back to the file
                fs.writeFileSync(collectionFilePath, JSON.stringify(collection, null, 2), 'utf8');
                console.log(`Updated ${file}`);
            } else {
                console.error(`Skipping ${file} because it doesn't have an 'item' property.`);
            }
        } catch (error) {
            console.error(`Error processing ${file}: ${error.message}`);
        }
    }
});

console.log('Postman collection URLs and variables updated successfully.');
