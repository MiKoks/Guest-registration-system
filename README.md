# Guest Registration System

Spring Boot + MySQL backend for managing events and participants (private/company) with registrations and DB-managed payment methods.

## Tech stack
- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL 8 (Docker)
- Gradle

---

## Requirements covered (high level)
- List past and future events (+ participant count)
- Add future events
- Delete future events (cascade delete registrations)
- List participants for a specific event
- Add participant to an event (PRIVATE or COMPANY) + payment method (DB managed)
- Remove participant from event
- Participant detail view + update (PRIVATE / COMPANY)
- Estonian personal code validation for PRIVATE participants
- Payment methods are managed in DB (no source code changes required)

---

## Run locally

### 1) Start MySQL (Docker)
Create a `docker-compose.yml` (or use existing) and start it:

docker compose up -d

### 2) Run Backend

./gradlew bootRun

### 3) Run tests

./gradlew test

### API endpoints (summary)

Events

GET /api/events – list events (past + future)

POST /api/events – create event (future date only)

DELETE /api/events/{eventId} – delete event (future only)

Event participants (registrations)

GET /api/events/{eventId}/participants – list participants for event

POST /api/events/{eventId}/participants – add participant + register to event

DELETE /api/events/{eventId}/participants/{participantId} – remove from event

Participant details

GET /api/participants/{participantId} – get participant details

PUT /api/participants/{participantId}/private – update private participant

PUT /api/participants/{participantId}/company – update company participant

### Extra DB info

localhost:3309

DB: guest_registration

user/pass: app/app

Schema + seed data loaded via Docker init scripts in db/init (runs only on fresh volume).