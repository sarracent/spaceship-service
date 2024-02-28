# Spaceships Service - Core Application

This repository hosts the core application for the management of spaceships and series within the `spaceshipsservice` 
project. The application provides secure RESTful APIs to manage spaceships and their associated series, allowing for 
operations such as retrieving, creating, updating, and deleting spaceships, as well as managing series related to these spaceships.

## Overview

The Spaceships Service is a secured application that provides various endpoints to manage the lifecycle of spaceships 
and series. It is designed with basic security configurations to protect the resources and ensure that only authenticated 
and authorized users have access to the functionalities.

## Features

### Spaceship Management
- **List Spaceships**: Paginated listing of all spaceships.
- **Get Spaceship by ID**: Retrieve details of a specific spaceship.
- **Create Spaceship**: Add a new spaceship to the system.
- **Update Spaceship**: Modify details of an existing spaceship.
- **Delete Spaceship**: Remove a spaceship from the system.
- **Search Spaceships**: Search for spaceships by name.
- **Manage Series for Spaceship**: Associate a series with a specific spaceship and list series related to a spaceship.

### Series Management
- **Create Series**: Create a new series record.
- **List All Series**: Retrieve all series records.

## Technical Details

- **Database**: Uses an H2 in-memory database for storing spaceship and series data.
- **Testing**: Includes unit testing for classes and integration tests for the service.
- **Logging Aspect**: Custom aspect for logging all operations
- **Caching**: Using Redis for frequently accessed data.
- **Security**: Basic security configuration to protect API endpoints.

## Optional Improvements
         
- **Database DDL Scripts**: Use of libraries like Liquibase for database version control.
- **Dockerization**: Containerization of the application for easy deployment.
- **API Documentation**: Detailed API documentation with tools like Swagger.
- **Message Broker Integration**: Implementation of a producer pattern using Kafka.

## Usage Examples

Below is an example of how to interact with the Spaceships API using cURL. This example demonstrates creating 
a new spaceship with Basic Authentication.

### Create a New Spaceship
  ```bash
    curl --location 'http://localhost:8080/spaceships' \
    --header 'Content-Type: application/json' \
    --header 'Authorization: Basic YWRtaW46YWRtaW4=' \
    --header 'Cookie: JSESSIONID=4B2221B24ADA6AF2BD33C36F9D0EA43B' \
    --data '{
        "manufacturingDate": "1992-08-27",
        "name": "x-zc",
        "model": "rx-111",
        "color": "blue"
    }'
   ```

## Setup
### Installation
1. **Clone the repository**:
    ```bash
    git clone https://github.com/sarracent/spaceship-service.git
    cd spaceshipsservice
    ```

2. **Build the application**:
    ```bash
    mvn clean install
    ```
   
3. **Build Docker images**:
    ```bash
    docker-compose build
    ```
   
4. **Run the application using docker compose**:
    ```bash
    docker-compose up -d
    ```
   ```bash
    docker-compose logs -f
    ```
   ```bash
    docker-compose down
    ```


## API Endpoints

### Spaceships
- `GET /spaceships`: List all spaceships with pagination.
- `GET /spaceships/{id}`: Get a spaceship by ID.
- `DELETE /spaceships/{id}`: Delete a spaceship by ID.
- `GET /spaceships/search`: Search for spaceships by name.
- `POST /spaceships`: Create a new spaceship.
- `PUT /spaceships/{id}`: Update a spaceship by ID.
- `GET /spaceships/{id}/series`: List series associated with a spaceship.
- `POST /spaceships/{id}/series`: Associate a new series with a spaceship.

### Series
- `POST /series`: Create a new series.
- `GET /series`: List all series.

## Error Handling

The application uses custom exceptions to gracefully handle errors and provide informative responses to the client.

## Testing

### Unit Tests

Unit tests can be executed with:

```bash
mvn test
