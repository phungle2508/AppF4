const fs = require('fs');

// Function to flatten Postman collection
const flattenCollection = (collection) => {
    // Logic to flatten the collection into one folder per resource
    collection.item = collection.item.map(item => {
        // Example of flattening logic: Remove unnecessary nesting
        if (item.item) {
            return { ...item, item: item.item[0] }; // Flatten nested items
        }
        return item;
    });
    return collection;
};

// Read collection JSON file
const collectionFile = process.argv[2]; // File passed as argument
fs.readFile(collectionFile, 'utf-8', (err, data) => {
    if (err) {
        console.error('Error reading file:', err);
        process.exit(1);
    }

    const collection = JSON.parse(data);

    // Flatten the collection
    const flattenedCollection = flattenCollection(collection);

    // Write the modified collection back to file
    fs.writeFile(collectionFile, JSON.stringify(flattenedCollection, null, 2), (err) => {
        if (err) {
            console.error('Error writing file:', err);
            process.exit(1);
        }
        console.log(`Flattened collection saved to ${collectionFile}`);
    });
});
