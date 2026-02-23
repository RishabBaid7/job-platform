## 🏗️ System Architecture

```mermaid
flowchart TB

    %% Client
    Client[Client / Frontend]

    %% Gateway Layer
    Gateway[API Gateway<br/>Spring Cloud Gateway]

    %% Service Discovery
    Eureka[Eureka Service Registry]

    %% Microservices
    Auth[Auth Service]
    User[User Service]
    Job[Job Service]
    App[Application Service]
    Notify[Notification Service]

    %% Infrastructure
    Kafka[(Kafka)]
    Postgres[(PostgreSQL)]

    %% Client Flow
    Client --> Gateway

    %% Gateway routing
    Gateway --> Auth
    Gateway --> User
    Gateway --> Job
    Gateway --> App

    %% Service Discovery
    Auth --- Eureka
    User --- Eureka
    Job --- Eureka
    App --- Eureka
    Gateway --- Eureka

    %% Database usage
    Auth --> Postgres
    User --> Postgres
    Job --> Postgres
    App --> Postgres
    Notify --> Postgres

    %% Event-driven flow
    App -- application.created --> Kafka
    Job -- job.created --> Kafka
    Kafka --> Notify
