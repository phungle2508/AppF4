const fs = require('fs');

// Function to add Authorization header to each request
function add_auth_header_to_requests(items) {
  if (Array.isArray(items)) {
    for (const item of items) {
      if (item.request) {
        // Ensure the 'header' array exists
        if (!item.request.header) item.request.header = [];

        // Remove any existing Authorization header
        item.request.header = item.request.header.filter(h => h.key !== 'Authorization');

        // Add Authorization header using environment variable reference
        item.request.header.push({
          key: "Authorization",
          value: "Bearer {{access_token}}", // Reference to the Postman environment variable 'token'
          type: "text"
        });
      }

      // If the item contains other nested items, call the function recursively
      if (item.item) {
        add_auth_header_to_requests(item.item);
      }
    }
  } else {
    console.error('Error: items is not an array or is missing');
  }
}

// Path to the Postman collection and token (not required for this version)
const filePath = process.argv[2];

// Read the Postman collection file
fs.readFile(filePath, 'utf8', (err, data) => {
  if (err) {
    console.error('Error reading the file:', err);
    return;
  }

  let collectionData;
  try {
    collectionData = JSON.parse(data);
  } catch (e) {
    console.error('Error parsing the JSON file:', e);
    return;
  }

  // Check if 'item' exists in the parsed data and is an array
  if (!Array.isArray(collectionData.item)) {
    console.log('No "item" array found. Creating one.');
    collectionData.item = []; // Create an empty item array if missing
  }

  // Add the Authorization header to all requests
  add_auth_header_to_requests(collectionData.item);

  // Save the modified collection back to the file
  fs.writeFile(filePath, JSON.stringify(collectionData, null, 2), (err) => {
    if (err) {
      console.error('Error writing the file:', err);
      return;
    }
    console.log('Authorization header added successfully to all requests.');
  });
});
