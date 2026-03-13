# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Running the Platform

The entire stack (all microservices + infrastructure) is managed via Docker Compose:

```bash
# Start everything (builds images on first run or after changes)
docker compose up --build

# Start in background
docker compose up --build -d

# Stop everything
docker compose down
```

**Frontend (local dev):**
```bash
cd job-platform-ui
npm install
npm start        # runs on http://localhost:3000
npm test         # run tests
npm run build    # production build
```

**Backend (individual service):**
```bash
cd services/<service-name>
./mvnw spring-boot:run   # requires local PostgreSQL/Kafka
./mvnw test              # run tests for that service
./mvnw package           # build JAR
```

## Architecture Overview

Event-driven microservices platform with a React frontend hitting a Spring Cloud API Gateway.

### Services and Ports

| Service | Port | Role |
|---|---|---|
| service-registry | 8761 | Netflix Eureka — service discovery |
| gateway-service | 8080 | Spring Cloud Gateway — single entry point, JWT filter |
| auth-service | 8085 | Registration/login, JWT token issuance |
| user-service | 8081 | User profile management |
| job-service | 8082 | Job listings, filtering, pagination |
| application-service | 8083 | Job applications, status tracking |
| notification-service | 8084 | Email notifications via Kafka events |

### Request Flow

All client requests go through the API Gateway on port 8080. The gateway applies `JwtAuthenticationFilter` to all routes except `/auth/**`. Services register with Eureka for dynamic discovery.

### Async Communication (Kafka)

Three topics drive cross-service workflows:
- `user.registered` — produced by auth-service, consumed by user-service (creates profile)
- `job.created` — produced by job-service
- `application.created` — produced by application-service, consumed by notification-service (sends email)

Kafka consumers use `@RetryableTopic` (3 attempts, 2s exponential backoff) and a `ProcessedEvent` table for idempotency (duplicate detection).

### Data Layer

Single PostgreSQL database (`jobdb`) shared by all services, with service-specific tables. Services use Spring Data JPA.

### Frontend

React SPA communicating exclusively with the gateway (`http://localhost:8080`). An Axios interceptor attaches the JWT Bearer token from `AuthContext` to every request. Uses React Router v7 for navigation and `jwt-decode` for reading token claims.

## Key Environment Variables

| Variable | Used by | Purpose |
|---|---|---|
| `JWT_SECRET` | auth-service, gateway-service | JWT signing/verification key |
| `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD` | notification-service | SMTP email sending |

Defaults are provided in `application.properties` for local/docker use; override in `docker-compose.yml` for production.

## Tech Stack

- **Java 17**, Spring Boot 3.5.10, Spring Cloud 2025.0.1
- **Spring Cloud Gateway** (routing), **Netflix Eureka** (discovery), **Spring Kafka**, **Spring Security + JWT** (jjwt 0.11.5), **Spring Data JPA**
- **PostgreSQL 15**, **Apache Kafka** (Confluent images)
- **React 19**, React Router 7, Axios, jwt-decode
- **Docker / Docker Compose 3.8** — multi-stage Maven builds (JDK → JRE)
