# Task Web App
## Overview

**task-web-app** is a web application designed to help you create and manage tasks. 
Built using Ktor, this app leverages a database backend for persistent storage. 
The entire application is containerized using Docker and Docker Compose.

## Features

- **Task Management**: Create, update, delete, and manage tasks with ease.
- **Ktor Framework**: Leveraging the asynchronous capabilities of Ktor for efficient and scalable server performance.
- **Database Integration**: Persistent task storage using postgresql or mongodb.
- **Docker & Docker Compose**: Simplified setup and deployment with containerization.

## Prerequisites

To run this application, you will need:

- Docker
- Docker Compose

## Getting Started

1. **Clone the repository:**

    ```bash
    git clone https://github.com/denisgulev/ktor-task-web-app.git
    cd ktor-task-web-app
    ```

2. **Define a '.env' file with the variables required by the database**
   (see the file .env.example for reference)

3. **Build and run the application using Docker Compose:**
   1. make 'gradlew' executable on UNIX systems
   ```bash
   chmod +x gradlew
   ```
   2. build the application as a fatJar
   ```bash
   ./gradlew :buildFatJar 
   ```
   3. run 'docker compose up -d'
   ```bash
   docker compose up -d
   ```

4. **Access the application:**

   Open your web browser and navigate to `http://localhost:8080/static/index.html` to start using the task manager.

## License

This project is licensed under the MIT License.

## Contact

For questions or suggestions, please open an issue in this repository or contact the maintainers directly.

---
