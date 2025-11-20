# MDD - Monde de Dév

MDD (Monde de Dév) is a social network allowing developers to subscribe to technical topics, create articles, and comment on other members' posts.

## Prerequisites

- **Java 21**
- **Maven**
- **MySQL**
- **Node.js**
- **Angular CLI 19** 


## Tech Stack

### Backend
- **Java 21.0.7**
- **Spring Boot 3.5.7**
- **Maven 3.9.11**
- **Spring Security**
- **Spring Data JPA**
- **MySQL 8.0.43**

### Frontend
- **Angular 19.2.19**
- **TypeScript**
- **Angular Material**
- **RxJS**


## Installation

### 1. Clone the repository

```bash
git clone https://github.com/cecile-umecker/Developpez-une-application-full-stack-complete.git
cd Developpez-une-application-full-stack-complete
```

### 2. Database configuration

Create a MySQL database:

```sql
CREATE DATABASE mdd;
USE mdd;
CREATE USER 'mdd_user'@'localhost' IDENTIFIED BY 'password';
GRANT ALL PRIVILEGES ON mdd.* TO 'mdd_user'@'localhost';
FLUSH PRIVILEGES;
```

Create a .env file at the root of the /back folder and define your secret key in the JWT_SECRET field.

```properties
# Database
DB_URL=jdbc:mysql://localhost:3306/mdd?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
DB_USER=mdd_user
DB_PASSWORD=password

# JWT
JWT_SECRET=your_secret_key_with_minimum_32_characters_here
JWT_EXPIRATION=3600000

```

> **Note**: Database tables are automatically created by JPA from Java entities and initialized with sample data from `back/src/main/resources/data.sql` on first run.

### 3. Backend installation

```bash
cd back
mvn clean install -DskipTests
mvn spring-boot:run
```

The backend will be exposed at [http://localhost:8080/api](http://localhost:8080/api)

### 4. Frontend installation

```bash
cd front
npm install
npm start
```

The frontend will be accessible at [http://localhost:4200](http://localhost:4200)


## API Endpoints

The REST API is accessible at: [http://localhost:8080/api](http://localhost:8080/api)

### Authentication

| Method | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/api/auth/register` | Register a new user |
| `POST` | `/api/auth/login` | Login (returns HTTP-only cookies) |
| `POST` | `/api/auth/refresh` | Refresh access token |
| `POST` | `/api/auth/logout` | Logout |

### User

| Method | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/user/me` | Get current user information |
| `PUT` | `/api/user/me` | Update user profile |

### Topics

| Method | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/topic` | List all available topics |
| `GET` | `/api/topic/my` | List topics the user is subscribed to |
| `POST` | `/api/topic/{id}/subscription` | Subscribe to a topic |
| `DELETE` | `/api/topic/{id}/subscription` | Unsubscribe from a topic |

### Feed & Posts

| Method | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/feed` | Get news feed (posts from subscribed topics) |
| `GET` | `/api/post/{id}` | Get post details |
| `POST` | `/api/post` | Create a new post |

### Comments

| Method | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/api/post/{id}/comments` | List post comments |
| `POST` | `/api/post/{id}/comments` | Add a comment to a post |


### API documentation (Postman)

[Import](https://learning.postman.com/docs/getting-started/importing-and-exporting/importing-and-exporting-overview/#importing-data-into-postman) the Postman collection located at `postman/MDD.postman_collection.json`.


## Authentication

The API uses JWT authentication with **HTTP-only cookies** for enhanced security:

1. Upon login (`/api/auth/login`), two cookies are created:
   - `access_token` - Access token (short duration)
   - `refresh_token` - Refresh token (long duration)

2. Cookies are automatically sent with each request

3. To refresh the access token, call `/api/auth/refresh`

**Note**: In Postman, make sure the "Send cookies" option is enabled for requests to work properly.

## Documentation

### Backend code documentation (Javadoc)

Generate Java documentation:

```bash
cd back
mvn clean javadoc:javadoc
```

Documentation will be available at `back/target/reports/apidocs/index.html`

### Frontend code documentation (Compodoc)

Generate Angular documentation:

```bash
cd front
npx compodoc -p tsconfig.json
```

Documentation will be available at `front/documentation/index.html`