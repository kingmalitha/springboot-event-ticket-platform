# Spring Boot Event Ticket Platform

A comprehensive, production-ready REST API built with Spring Boot 3.5.5 for managing events, tickets, and ticket validation. The platform provides a complete solution for event organizers to manage events and ticket types, while allowing attendees to purchase, view, and validate tickets via QR codes.

## 🎯 Features

### Event Management

- **Create Events**: Organizers can create new events with detailed information
- **List Events**: Browse personal events (organizers) or published events (attendees)
- **Search Events**: Full-text search across published events
- **Update Events**: Modify event details
- **Delete Events**: Remove events from the system
- **Pagination Support**: Efficient data retrieval with pagination

### Ticket Management

- **Ticket Types**: Define multiple ticket types per event with pricing and availability
- **Ticket Purchase**: Attendees can purchase tickets with inventory management
- **Ticket Listing**: View purchased tickets with filtering and pagination
- **Sold Out Prevention**: Automatic prevention of overselling with inventory tracking

### Ticket Validation

- **QR Code Generation**: Automatic QR code generation for each ticket
- **QR Code Retrieval**: Download QR codes for validation purposes
- **Ticket Validation**: Validate tickets via QR code scanning with status tracking

### Security

- **OAuth 2.0 Resource Server**: JWT-based authentication using Keycloak
- **Role-Based Access Control**: Secure endpoints with proper authorization
- **User Provisioning**: Automatic user provisioning from JWT claims
- **Secure API**: All endpoints require authentication

### Observability & Monitoring

- **Prometheus Metrics**: Built-in application metrics
- **Health Checks**: Spring Boot Actuator endpoints for health monitoring
- **Structured Logging**: Integrated with Grafana/Loki for centralized logging
- **OTEL Integration**: OpenTelemetry support for distributed tracing

## 🏗️ Architecture

### Technology Stack

| Component          | Version/Technology          |
| ------------------ | --------------------------- |
| **Java**           | 21                          |
| **Spring Boot**    | 3.5.5                       |
| **Build Tool**     | Maven                       |
| **Database**       | PostgreSQL                  |
| **ORM**            | Spring Data JPA / Hibernate |
| **Mapping**        | MapStruct 1.6.3             |
| **Security**       | Spring Security + OAuth2    |
| **Authentication** | Keycloak                    |
| **QR Codes**       | ZXing 3.5.1                 |
| **Validation**     | Jakarta Bean Validation     |
| **Utilities**      | Lombok 1.18.36              |
| **Monitoring**     | Prometheus + Grafana + Loki |

### Project Structure

```
src/main/java/com/kingmalitha/springbooteventticketplatform/
├── SpringbootEventTicketPlatformApplication.java   # Main application class
├── config/                                          # Configuration classes
│   ├── JpaConfig.java                              # JPA/Hibernate configuration
│   ├── JwtAuthenticationConverter.java             # JWT to Authentication converter
│   ├── QrCodeConfig.java                           # QR code generation config
│   └── SecurityConfig.java                         # Spring Security configuration
├── controllers/                                     # REST API Controllers
│   ├── EventController.java                        # Event management endpoints
│   ├── PublishedEventController.java               # Public event browsing endpoints
│   ├── TicketController.java                       # Ticket retrieval & QR codes
│   ├── TicketTypeController.java                   # Ticket type management
│   ├── TicketValidationController.java             # Ticket validation endpoints
│   └── GlobalExceptionHandler.java                 # Centralized exception handling
├── domain/                                          # Domain models
│   ├── CreateEventRequest.java                     # Event creation DTO
│   ├── UpdateEventRequest.java                     # Event update DTO
│   ├── CreateTicketTypeRequest.java                # Ticket type creation DTO
│   ├── UpdateTicketTypeRequest.java                # Ticket type update DTO
│   ├── dtos/                                       # Data transfer objects
│   └── entities/                                   # JPA entities
├── exceptions/                                      # Custom exceptions
│   ├── EventNotFoundException.java
│   ├── TicketNotFoundException.java
│   ├── TicketSoldOutException.java
│   ├── TicketTypeNotFoundException.java
│   ├── QrCodeNotFoundException.java
│   ├── QrCodeGenerationException.java
│   └── UserNotFoundException.java
├── filters/                                         # Servlet filters
│   └── UserProvisioningFilter.java                 # JWT-based user provisioning
├── mappers/                                         # MapStruct mappers
│   ├── EventMapper.java                            # Event entity ↔ DTO mapping
│   ├── TicketMapper.java                           # Ticket entity ↔ DTO mapping
│   └── TicketValidationMapper.java                 # Validation entity ↔ DTO mapping
├── repositories/                                    # Spring Data JPA repositories
│   ├── EventRepository.java
│   ├── TicketRepository.java
│   ├── TicketTypeRepository.java
│   ├── TicketValidationRepository.java
│   ├── QrCodeRepository.java
│   └── UserRepository.java
├── services/                                        # Business logic services
│   ├── EventService.java                           # Event management logic
│   ├── TicketService.java                          # Ticket management logic
│   ├── TicketTypeService.java                      # Ticket type logic
│   ├── QrCodeService.java                          # QR code generation & retrieval
│   ├── TicketValidationService.java                # Ticket validation logic
│   └── impl/                                       # Service implementations
└── util/                                            # Utility classes
    └── JwtUtil.java                                # JWT parsing utilities
```

## 🚀 Getting Started

### Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **Docker & Docker Compose** (for containerized setup)
- **PostgreSQL 12+** (if not using Docker)
- **Git**

### Installation

#### 1. Clone the Repository

```bash
git clone https://github.com/kingmalitha/springboot-event-ticket-platform.git
cd springboot-event-ticket-platform
```

#### 2. Set Up Using Docker Compose (Recommended)

The `docker-compose.yml` includes all required services:

```bash
docker-compose up -d
```

This will start:

- **PostgreSQL** database (port 5432)
- **Keycloak** identity provider (port 9090)
- **Grafana/Loki** for monitoring (port 3000)
- **Application** (port 8080)

**Default Keycloak Credentials:**

- Username: `admin`
- Password: `password`

Access Keycloak admin console: `http://localhost:9090`

#### 3. Manual Setup (Local Development)

**a. Database Setup**

```bash
# Create PostgreSQL database
createdb event_ticket_platform

# Update src/main/resources/application.properties with your database credentials
```

**b. Build the Application**

```bash
mvn clean install
```

**c. Run the Application**

```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## 📡 API Endpoints

### Authentication

All endpoints require OAuth 2.0 JWT token in the `Authorization` header:

```
Authorization: Bearer <your-jwt-token>
```

### Event Management

| Method   | Endpoint                          | Description               | Role      |
| -------- | --------------------------------- | ------------------------- | --------- |
| `POST`   | `/api/v1/events`                  | Create a new event        | Organizer |
| `GET`    | `/api/v1/events`                  | List organizer's events   | Organizer |
| `GET`    | `/api/v1/events/{eventId}`        | Get event details         | Organizer |
| `PUT`    | `/api/v1/events/{eventId}`        | Update event              | Organizer |
| `DELETE` | `/api/v1/events/{eventId}`        | Delete event              | Organizer |
| `GET`    | `/api/v1/published-events`        | List all published events | Any User  |
| `GET`    | `/api/v1/published-events/search` | Search published events   | Any User  |

### Ticket Management

| Method | Endpoint                              | Description         | Role     |
| ------ | ------------------------------------- | ------------------- | -------- |
| `GET`  | `/api/v1/tickets`                     | List user's tickets | Attendee |
| `GET`  | `/api/v1/tickets/{ticketId}`          | Get ticket details  | Attendee |
| `GET`  | `/api/v1/tickets/{ticketId}/qr-codes` | Download QR code    | Attendee |

### Ticket Types

| Method   | Endpoint                                | Description        | Role      |
| -------- | --------------------------------------- | ------------------ | --------- |
| `POST`   | `/api/v1/events/{eventId}/ticket-types` | Create ticket type | Organizer |
| `GET`    | `/api/v1/events/{eventId}/ticket-types` | List ticket types  | Organizer |
| `PUT`    | `/api/v1/ticket-types/{ticketTypeId}`   | Update ticket type | Organizer |
| `DELETE` | `/api/v1/ticket-types/{ticketTypeId}`   | Delete ticket type | Organizer |

### Ticket Validation

| Method | Endpoint                                    | Description                 |
| ------ | ------------------------------------------- | --------------------------- |
| `POST` | `/api/v1/ticket-validations/validate`       | Validate ticket via QR code |
| `GET`  | `/api/v1/ticket-validations/{validationId}` | Get validation details      |

### Monitoring & Health

| Endpoint               | Description               |
| ---------------------- | ------------------------- |
| `/actuator/health`     | Application health status |
| `/actuator/info`       | Application info          |
| `/actuator/prometheus` | Prometheus metrics        |

## 🔌 Configuration

### Application Configuration (`application.yml`)

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health, info, prometheus
  endpoint:
    prometheus:
      enabled: true
```

### Environment Variables (for Docker)

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/postgres
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI=http://keycloak:8080/realms/event-ticket-platform
OTEL_EXPORTER_OTLP_ENDPOINT=http://lgtm:4318
OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf
OTEL_RESOURCE_ATTRIBUTES=service.name=springboot-event-ticket-platform
```

## 🧪 Testing

Run the test suite:

```bash
mvn test
```

Test files are located in `src/test/java/`.

## 📊 Monitoring & Observability

### Prometheus Metrics

Access metrics at: `http://localhost:8080/actuator/prometheus`

### Grafana Dashboard

Access Grafana at: `http://localhost:3000`

Default credentials:

- Username: `admin`
- Password: `admin`

### Health Check

```bash
curl http://localhost:8080/actuator/health
```

## 🔐 Security Features

- **JWT-based Authentication**: Secure token-based authentication
- **OAuth2 Resource Server**: Integrates with Keycloak for centralized identity management
- **CORS Configuration**: Configurable cross-origin request handling
- **Input Validation**: Jakarta Bean Validation on all request bodies
- **Exception Handling**: Centralized exception handling with proper HTTP status codes

## 📝 API Examples

### Create Event

```bash
curl -X POST http://localhost:8080/api/v1/events \
  -H "Authorization: Bearer <your-jwt-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Tech Conference 2025",
    "description": "Annual tech conference",
    "startDate": "2025-06-01T09:00:00",
    "endDate": "2025-06-03T17:00:00",
    "location": "Convention Center",
    "published": false
  }'
```

### List Published Events

```bash
curl -X GET "http://localhost:8080/api/v1/published-events?page=0&size=10" \
  -H "Authorization: Bearer <your-jwt-token>"
```

### Search Events

```bash
curl -X GET "http://localhost:8080/api/v1/published-events/search?query=tech&page=0&size=10" \
  -H "Authorization: Bearer <your-jwt-token>"
```

### Get Ticket QR Code

```bash
curl -X GET http://localhost:8080/api/v1/tickets/{ticketId}/qr-codes \
  -H "Authorization: Bearer <your-jwt-token>" \
  -o ticket-qr-code.png
```

## 🐳 Docker Support

The project includes a `Dockerfile` for containerization and `docker-compose.yml` for orchestrating all services.

**Build Docker Image:**

```bash
docker build -t springboot-event-ticket-platform:latest .
```

**Run with Docker Compose:**

```bash
docker-compose up -d
```

**View Logs:**

```bash
docker-compose logs -f app
```

**Stop Services:**

```bash
docker-compose down
```

## 📦 Dependencies

Key dependencies included:

- Spring Boot 3.5.5
- Spring Security + OAuth2
- Spring Data JPA
- MapStruct (for DTOs)
- Lombok (boilerplate reduction)
- ZXing (QR code generation)
- Prometheus (metrics)
- PostgreSQL driver
- Jakarta Validation

See `pom.xml` for complete dependency list.

## 👨‍💼 Author

**Kingmalitha**

- GitHub: [@kingmalitha](https://github.com/kingmalitha)
- Project Repository: [springboot-event-ticket-platform](https://github.com/kingmalitha/springboot-event-ticket-platform)

---

**Happy coding! 🎉**
