# Goodreads-like Book Management System

This is a **Goodreads-inspired book management system** built using **Spring Boot** and **Java**. The system allows users to manage books, authors, and publishers, providing basic CRUD operations for each entity. The project follows a **RESTful API** architecture and utilizes **JPA** for database operations.

## Features

- **Author Management:**
  - Create, update, and delete author records.
  - Retrieve information about authors.
  - View books associated with a specific author.

- **Book Management:**
  - Create, update, and delete book records.
  - Retrieve book details by ID.
  - View the publisher and authors of a specific book.

- **Publisher Management:**
  - Create, update, and delete publisher records.
  - Retrieve publisher details by ID.
  - Automatically handle book references when deleting a publisher.

## Technologies Used

- **Spring Boot**: Provides the backbone for the RESTful APIs.
- **Spring Data JPA**: For database interactions.
- **Hibernate**: Object-relational mapping.
- **MySQL/PostgreSQL**: Database integration (you can configure it based on your setup).
- **JSON**: Data format for communication between frontend and backend.
- **Java Persistence API (JPA)**: For ORM functionality.

## Project Structure

- **Controllers**: Handle HTTP requests and map them to appropriate service methods.
- **Services**: Business logic for handling publishers, books, and authors.
- **Repositories**: Interfaces for database operations using JPA.
- **Models**: Entities representing the `Book`, `Author`, and `Publisher`.

## How to Run

1. Clone the repository:

    ```bash
    git clone https://github.com/your-username/goodreads-clone.git
    ```

2. Navigate into the project directory:

    ```bash
    cd goodreads-clone
    ```

3. Configure the database connection in `application.properties`.

4. Run the application:

    ```bash
    ./mvnw spring-boot:run
    ```

## API Endpoints

### Authors
- `GET /authors`: Fetch all authors.
- `POST /authors`: Create a new author.
- `GET /authors/{id}`: Fetch author details by ID.
- `PUT /authors/{id}`: Update author details.
- `DELETE /authors/{id}`: Delete an author by ID.
- `GET /authors/{authorId}/books`: Fetch books by an author.

### Books
- `GET /books`: Fetch all books.
- `POST /publishers/books`: Create a new book.
- `GET /books/{bookId}`: Fetch book details by ID.
- `PUT /publishers/books/{bookId}`: Update book details.
- `DELETE /books/{bookId}`: Delete a book by ID.
- `GET /books/{bookId}/publisher`: Get publisher details of a book.
- `GET /books/{bookId}/authors`: Get author details of a book.

### Publishers
- `GET /publishers`: Fetch all publishers.
- `POST /publishers`: Create a new publisher.
- `GET /publishers/{publisherId}`: Fetch publisher details by ID.
- `PUT /publishers/{publisherId}`: Update publisher details.
- `DELETE /publishers/{publisherId}`: Delete a publisher by ID.

## Future Enhancements

- Add user authentication and authorization.
- Integrate search functionality for books.
- Implement pagination for large datasets.
- Add unit tests to improve coverage.
