# Car Registry System

## Overview
The Car Registry System is a Spring Boot-based application designed to manage a registry of cars and brands. It provides RESTful endpoints for creating, reading, updating, and deleting car and brand records. The system also includes user authentication and role-based access control using JWT for secure operations.

## Features
- **Car Management:** Add, view, update, and delete car records.
- **Brand Management:** Manage car brands, including creating, viewing, updating, and deleting brand records.
- **User Authentication:** Secure user authentication using JWT.
- **Role-Based Access Control:** Different access levels for CLIENT and VENDOR roles.
- **File Upload:** Supports file upload functionality for user profile images.

## Technologies Used
- **Spring Boot:** Framework for building the application.
- **Spring Security:** For authentication and authorization.
- **JWT (JSON Web Tokens):** For secure user sessions.
- **Hibernate:** ORM for database interactions.
- **MySQL:** Database for storing car, brand, and user information.
- **MapStruct:** For mapping between entity and DTO classes.
- **JUnit & Mockito:** For testing the application.

## Endpoints
### Car Endpoints
- `GET /cars` - Retrieve all cars (Accessible by CLIENT and VENDOR)
- `GET /cars/{id}` - Retrieve a car by ID (Accessible by CLIENT and VENDOR)
- `POST /cars/add` - Add a new car (Accessible by VENDOR)
- `PUT /cars/{id}` - Update a car by ID (Accessible by VENDOR)
- `DELETE /cars/{id}` - Delete a car by ID (Accessible by VENDOR)

### Brand Endpoints
- `GET /brands` - Retrieve all brands (Accessible by CLIENT and VENDOR)
- `GET /brands/{id}` - Retrieve a brand by ID (Accessible by CLIENT and VENDOR)
- `POST /brands/add` - Add a new brand (Accessible by VENDOR)
- `PUT /brands/{id}` - Update a brand by ID (Accessible by VENDOR)
- `DELETE /brands/{id}` - Delete a brand by ID (Accessible by VENDOR)

### User Endpoints
- `POST /signup` - User registration
- `POST /login` - User login

### File Upload Endpoint
- `POST /api/upload` - Upload a file (Accessible by authenticated users)

## Getting Started
### Prerequisites
- Java 11 or higher
- Maven
- MySQL

### Installation
1. **Clone the repository:**
    ```bash
    git clone https://github.com/yourusername/car-registry-system.git
    cd car-registry-system
    ```

2. **Set up the MySQL database:**
    ```sql
    CREATE DATABASE car_registry;
    ```

3. **Update application.properties:**
    Configure your MySQL database credentials in `src/main/resources/application.properties`.

4. **Build the application:**
    ```bash
    mvn clean install
    ```

5. **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

### Testing
Run the test cases using:
```bash
mvn test

