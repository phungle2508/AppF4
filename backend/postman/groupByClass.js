const fs = require('fs');

// Function to reorganize Postman collection by class
const reorganizeByClass = (collection) => {
    const reorganizedItems = [];
    const classMap = new Map();

    // First pass: Group items by class
    collection.item.forEach(item => {
        if (item.item && Array.isArray(item.item)) {
            item.item.forEach(method => {
                if (method.item && Array.isArray(method.item)) {
                    method.item.forEach(endpoint => {
                        if (endpoint.request) {
                            const className = item.name;
                            if (!classMap.has(className)) {
                                classMap.set(className, {
                                    name: className,
                                    item: []
                                });
                            }
                            classMap.get(className).item.push(endpoint);
                        }
                    });
                }
            });
        }
    });

    // Convert map to array
    classMap.forEach((value) => {
        reorganizedItems.push(value);
    });

    // Update the collection with reorganized items
    collection.item = reorganizedItems;
    return collection;
};

// Read collection JSON file
const collectionFile = process.argv[2]; // File passed as argument
if (!collectionFile) {
    console.error('Please provide a collection file path as an argument');
    process.exit(1);
}

fs.readFile(collectionFile, 'utf-8', (err, data) => {
    if (err) {
        console.error('Error reading file:', err);
        process.exit(1);
    }

    try {
        const collection = JSON.parse(data);
        
        // Reorganize the collection
        const reorganizedCollection = reorganizeByClass(collection);

        // Write the modified collection back to file
        fs.writeFile(collectionFile, JSON.stringify(reorganizedCollection, null, 2), (err) => {
            if (err) {
                console.error('Error writing file:', err);
                process.exit(1);
            }
            console.log(`Collection reorganized and saved to ${collectionFile}`);
        });
    } catch (error) {
        console.error('Error processing collection:', error);
        process.exit(1);
    }
});
