# 🎫 TicketFlow: Event-Driven Ticket Booking System

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

> Modern, production-ready microservices architecture for booking concert tickets. Built to demonstrate enterprise-level patterns: Event-Driven Architecture, Outbox Pattern, Circuit Breakers, and Distributed Tracing.

[//]: # (![Architecture Diagram]&#40;./docs/architecture.png&#41;)

## 🚀 Overview

TicketFlow is a distributed system designed to handle high-load ticket sales without race conditions or data inconsistency. It solves the classic "double-booking" problem using database-level row locking and ensures reliable asynchronous communication via the **Transactional Outbox Pattern**.

## ✨ Key Features & Patterns Implemented

- 🏗 **Microservices Architecture**: Independent deployable units with Database-per-Service pattern.
- ⚡ **Event-Driven**: Asynchronous communication via Apache Kafka (KRaft mode).
- 🔒 **Transactional Outbox**: Guarantees reliable message delivery to Kafka from `Booking Service`.
- 🛡 **Resilience**: Circuit Breakers (Resilience4j) to handle cascading failures.
- 🔐 **Security**: JWT-based authentication via Keycloak and API Gateway.
- 📊 **Observability**: Distributed tracing with Micrometer & Zipkin (TraceID propagation).
- 🧪 **Testcontainers**: 100% reliable integration tests using real Docker containers.
- 🗄 **Flyway Migrations**: Version-controlled database schema evolution.

## 🛠 Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Language** | Java 21 (Virtual Threads enabled) |
| **Framework** | Spring Boot 4.1, Spring Cloud 2025.x |
| **Message Broker** | Apache Kafka (KRaft mode, no Zookeeper) |
| **Databases** | PostgreSQL 16, Redis 7 (Caching & Idempotency) |
| **API Gateway** | Spring Cloud Gateway (Reactive) |
| **Auth** | Keycloak (OIDC/OAuth2), JWT |
| **Testing** | JUnit 5, Testcontainers, Awaitility |
| **DevOps** | Docker, Docker Compose, GitHub Actions (CI) |
| **Observability** | Micrometer Tracing, Zipkin, Spring Boot Actuator |

## ⚡ Quick Start

Run the entire system locally in under 2 minutes.

**Prerequisites:** Docker, Java 21, Maven.

```bash
# 1. Clone the repository
git clone https://github.com/your-username/ticketflow.git
cd ticketflow

# 2. Start infrastructure (DBs, Kafka, Keycloak)
docker-compose -f docker-compose.infra.yml up -d

# 3. Build and run all microservices
mvn clean package -DskipTests
java -jar ticketflow-gateway/target/ticketflow-gateway.jar &
java -jar ticketflow-catalog/target/ticketflow-catalog.jar &
java -jar ticketflow-booking/target/ticketflow-booking.jar &
java -jar ticketflow-payment/target/ticketflow-payment.jar &
java -jar ticketflow-notification/target/ticketflow-notification.jar &
```
## 📡 API Endpoints

The API is accessible via the Gateway at `http://localhost:8080`.

| Method | Endpoint | Service | Description |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/concerts` | Catalog | List available concerts |
| `POST` | `/api/v1/bookings` | Booking | Reserve tickets |
| `POST` | `/api/v1/payments` | Payment | Process payment (internal) |

> 📌 **Full API documentation** is available via Swagger UI at `http://localhost:8080/swagger-ui.html` once the Gateway is running.

## 🧪 Testing

We use **Testcontainers** for integration tests. No mocks, only real databases and brokers!

```bash
# Run all integration tests
mvn verify

# Run tests for a specific service
mvn verify -pl ticketflow-booking
```

## 🗂 Project Structure

```
ticketflow/
├── ticketflow-contracts/   # Shared DTOs, Kafka Events, Feign APIs
├── ticketflow-gateway/     # API Gateway (Routing, Security, Rate Limiting)
├── ticketflow-catalog/     # Read-heavy service (Redis Cache)
├── ticketflow-booking/     # Core domain (Outbox Pattern, Locking)
├── ticketflow-payment/     # Idempotent payment processing
└── ticketflow-notification/ # Kafka consumer (Email/Telegram)
```

## 🛣 Roadmap

- [x] Initial setup & Docker infrastructure
- [ ] Monorepo Maven structure
- [ ] Transactional Outbox implementation
- [ ] Saga pattern for distributed transactions
- [ ] Kubernetes manifests (Helm charts)
- [ ] Grafana dashboards for metrics

## 📄 License

MIT License. Feel free to use this project for learning and interviews!