// Connect to the desired database
db = db.getSiblingDB('taskManagement');

// Create a collection
db.createCollection("users");
db.createCollection("tasks");

// Insert an admin user into the 'users' collection
db.users.insertOne({
    username: "admin",
    email: "admin@example.com",
    password: "$2y$12$vZdxNmBdLBsw82C4wyoEXuZ8qsvU6zM9p8BjM.EExbeqZ5VAOT882", // Replace with the hashed password from the bcrypt script ( == "admin")
    role: "ADMIN",
    permission: "ALL",
    createdAt: new Date(),
    updatedAt: new Date()
});

print("Initialization script executed successfully");
