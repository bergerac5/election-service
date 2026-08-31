# Election Service

## Overview

The election service is the core election-management component of the online-voting system. It manages election lifecycle data, candidate-to-position assignment, and election status transitions while integrating with the candidate service and Kafka-backed event processing pipeline.

This module runs on Java 25 with Spring Boot 3.5.0 and Spring Cloud 2025.0.0.

## Responsibilities

- Create, update, delete, and query elections
- Manage positions tied to an election
- Assign candidates to specific positions
- Track election status and closure events
- Publish `ElectionClosedEvent` to Kafka when an election ends
- Query candidate detail data via OpenFeign from the candidate service
- Enforce role-based access through Spring Security and JWT claims

## Tech Stack

- Java 25
- Spring Boot 3.5.0
- Spring Cloud 2025.0.0
- Spring Web MVC
- Spring Security
- Spring Data JPA
- PostgreSQL
- Spring Cloud Stream + Kafka
- OpenFeign
- Resilience4j
- JJWT
- Maven
- Spring Boot Actuator

## Runtime Configuration

The service is configured to run on:
- `http://localhost:8084`

Key settings live in `src/main/resources/application.yaml`.

### Database

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/vote_electiondb
    username: "add yours"
    password: "add yours"
    driver-class-name: org.postgresql.Driver
```

### Kafka / event stream

```yaml
spring:
  cloud:
    function:
      definition: electionClosed
    stream:
      bindings:
        electionClosed-out-0:
          destination: election-closed
          contentType: application/json
      kafka:
        binder:
          brokers: localhost:9092
          auto-create-topics: true
          configuration:
            spring.json.trusted.packages: com.online.voting.events
```

The service emits an election-closure event to the result pipeline when the scheduled closing logic triggers.

## Security Model

The service uses Spring Security with JWT-based authorization and role checks.

### Protected admin endpoints

Administrative operations are guarded with `@PreAuthorize("hasRole('ADMIN')")` and include:
- election creation
- election update
- election delete
- election status updates
- position creation
- position update
- position delete
- candidate assignment

### Public read endpoints

Public or broadly readable endpoints include:
- election lookup by title
- election details by ID
- all elections
- position lookup by ID or election
- bulk data fetches

## API Endpoints

### Election endpoints

#### Create election
- `POST /elections/createPosition`
- Requires `ADMIN`

#### Update election
- `PUT /elections/updateElection/{electionId}`
- Requires `ADMIN`

#### Delete election
- `DELETE /elections/deleteElection/{electionId}`
- Requires `ADMIN`

#### Get election by title
- `GET /elections/title/{title}`

#### Update election status
- `PATCH /elections/{electionId}/status`
- Requires `ADMIN`

#### Get all elections
- `GET /elections/allElections`

#### Get election by ID
- `GET /elections/{electionId}`

#### Get multiple elections by IDs
- `POST /elections/bulk`

### Position endpoints

#### Create position
- `POST /positions/createPosition`
- Requires `ADMIN`

#### Update position
- `PUT /positions/updatePosition/{positionId}`
- Requires `ADMIN`

#### Delete position
- `DELETE /positions/deletePosition/{positionId}`
- Requires `ADMIN`

#### Get all positions
- `GET /positions`

#### Get positions by election
- `GET /positions/election/{electionId}`

#### Assign candidate to position
- `POST /positions/{electionId}/positions/{positionId}/assign-candidate/{candidateId}`
- Requires `ADMIN`

#### Get position by ID
- `GET /positions/{positionId}`

#### Get multiple positions by IDs
- `POST /positions/bulk`

### Position-candidate endpoints

- `GET /position-candidates/position/{positionId}`
- `GET /position-candidates/election/{electionId}`

## Event-Driven Behavior

The election service publishes close-of-election events for downstream processing.

### Outgoing event

- `ElectionClosedEvent`
  - emitted when an election is closed by scheduled processing
  - payload includes `electionId` and `closedAt`
  - destination: `election-closed`

This event is consumed by the result service to update live and final tally logic.

## Integration Points

### Candidate integration

The service calls the candidate service through OpenFeign to fetch candidate records when building positions and election data.

### Gateway routing

The API gateway forwards election-related requests to this service through its configured gateway paths, including:
- `/elections/**`
- `/positions/**`
- `/position-candidates/**`

## Project Structure

```text
election-service/
├── src/
│   ├── main/
│   │   ├── java/com/online/voting/election/
│   │   │   ├── clients/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dtos/
│   │   │   ├── entity/
│   │   │   ├── handler/
│   │   │   ├── models/
│   │   │   ├── repository/
│   │   │   ├── service/
│   │   │   └── ElectionServiceApplication.java
│   │   └── resources/
│   │       └── application.yaml
│   └── test/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── HELP.md
├── README.md
└── target/
```

## Prerequisites

Before running the service, ensure the following are available:

- JDK 25 installed and active
- Maven wrapper available in the module
- PostgreSQL running locally
- database `vote_electiondb` available
- Kafka broker running at `localhost:9092`
- `common-events` dependency installed locally via Maven
- downstream candidate service reachable for OpenFeign calls

## Run Locally

From the module directory:

```powershell
cd d:\spring boot\online-voting-system\election-service
$env:JAVA_HOME='C:\Program Files\Java\jdk-25.0.4.1'
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path
.\mvnw clean test
.\mvnw spring-boot:run
```

The service will start on:
- `http://localhost:8084`

## Configuration Notes

- Database credentials and secrets are currently defined in local YAML files and should be externalized for production use.
- The service is designed for local microservice development and assumes a shared Kafka broker and PostgreSQL instance on the developer machine.
- The app enables bean override support in the configuration, which is useful in this local multi-service setup but should be reviewed if stricter production configuration control is required.

## Notes

- This service is part of the larger online-voting platform and depends on the shared event contract from the `common-events` module.
- The scheduling logic outside the REST controllers is responsible for election closure publication and downstream notifications.
- Local configuration values are development examples and should be replaced by environment variables or an external configuration store in a production deployment.

## Author / Project

This service is part of the Online Voting Microservice System.