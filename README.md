# CycleRun Coach

CycleRun Coach is a REST API that combines running records with information
about recovery, sleep, symptoms, energy, and menstrual cycle phase.

It lets a runner record completed workouts, submit a daily check-in, and request
a simple recommendation for the next training session.

## About the project

I started CycleRun Coach as a way to practice Java and Spring Boot while working
on an idea related to running and women's health.

The project started with a small domain model for workouts. I gradually added
services, repositories, REST controllers, validation, database persistence,
error handling, and recommendation rules.

I wanted it to be more than a basic CRUD exercise. The most interesting part
for me has been deciding how the different layers should work together and how
to keep business rules separate from HTTP and database code.

This repository contains the first portfolio version of the API. It runs
locally and is not currently deployed as a public service.

> The recommendations are simple training suggestions created for this project.
> They are not medical advice.

## What the API can do

- Create, read, update, and delete completed workouts.
- Create, read, update, and delete daily check-ins.
- Save workouts and check-ins in an H2 database.
- Keep local data after restarting the application.
- Find the most recent daily check-in by date.
- Generate a training recommendation from the latest check-in.
- Take energy, sleep, symptoms, and cycle phase into account.
- Validate incoming requests before processing them.
- Return clear and consistent error responses.
- Support manual testing through ready-to-run HTTP request files.

## Tech stack

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Jakarta Validation
- H2 Database
- Maven
- JUnit 5
- Mockito
- MockMvc
- Git and GitHub

## How the project is organized

I separated the project into layers so that each part has a clear
responsibility.

```text
HTTP request
    |
    v
Controller
    |
    +--> Request and response DTOs
    +--> DTO mapper
    |
    v
Service
    |
    v
Repository interface
    |
    v
JPA repository adapter
    |
    +--> JPA mapper
    |
    v
Spring Data JPA
    |
    v
H2 database
```

Controllers handle HTTP requests and responses. Services coordinate the use
cases and contain the recommendation logic. Repository interfaces define the
persistence operations needed by the services.

The JPA adapters implement those repository interfaces and translate between
domain objects and database entities.

This means that the domain classes do not need to know about JSON, controllers,
JPA, or the database.

I also kept in-memory repository implementations because they are useful for
isolated tests.

### Main packages

| Package | Responsibility |
| --- | --- |
| `controller` | REST endpoints and HTTP responses |
| `controller.dto` | Request and response models used by the API |
| `controller.mapper` | Conversion between DTOs and domain objects |
| `controller.error` | Consistent error responses |
| `domain` | Core models and their validation rules |
| `service` | Application use cases and recommendation rules |
| `repository` | Persistence interfaces |
| `repository.jpa` | JPA entities, mappers, and repository adapters |
| `repository.memory` | In-memory repositories used by isolated tests |

## Main user flow

The main flow of the API is:

```text
Create a daily check-in
        |
        v
Request a recommendation
        |
        v
Review the suggested workout and effort
        |
        v
Complete or adjust the session
        |
        v
Record the workout that was actually completed
```

I keep recommendations and workouts separate because they represent different
things.

A recommendation is a suggestion for a possible future session. A workout is a
session that has already happened. Requesting a recommendation does not
automatically create a workout.

## How recommendations work

The recommendation service evaluates the latest daily check-in using a list of
rules in a specific order.

I decided to check recovery-related information before cycle phase. For
example, if someone reports exhaustion or very little sleep, that should matter
more than the current phase.

| Priority | Condition | Recommendation | Maximum effort |
| --- | --- | --- | --- |
| 1 | Energy is `EXHAUSTED` | `RECOVERY_RUN` | 3 |
| 2 | Sleep is below 5 hours | `RECOVERY_RUN` | 3 |
| 3 | One or more symptoms are reported | `EASY_RUN` | 4 |
| 4 | Sleep is below 7 hours | `EASY_RUN` | 5 |
| 5 | Energy is `LOW` or sleep quality is `BAD` | `EASY_RUN` | 5 |
| 6 | Readiness is favorable | Depends on cycle phase | Depends on phase |
| 7 | None of the previous rules apply | `EASY_RUN` | 6 |

For readiness to be considered favorable, the check-in must have:

- `HIGH` or `VERY_HIGH` energy;
- `GOOD` or `EXCELLENT` sleep quality;
- at least 7 hours of sleep;
- no reported symptoms.

When those conditions are met, the cycle phase is considered:

| Cycle phase | Recommendation | Maximum effort |
| --- | --- | --- |
| `FOLLICULAR` | `TEMPO_RUN` | 8 |
| `OVULATORY` | `INTERVALS` | 6 |
| `LUTEAL` | `EASY_RUN` | 6 |
| `MENSTRUAL` | `EASY_RUN` | 5 |

I deliberately kept these rules simple and visible. The API returns a reason
with every recommendation so that the result is easy to understand and test.

## API endpoints

### Workouts

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/api/workouts` | List all workouts |
| `GET` | `/api/workouts/{id}` | Find a workout by ID |
| `POST` | `/api/workouts` | Create a completed workout |
| `PUT` | `/api/workouts/{id}` | Update a workout |
| `DELETE` | `/api/workouts/{id}` | Delete a workout |

### Daily check-ins

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/api/check-ins` | List all daily check-ins |
| `GET` | `/api/check-ins/latest` | Find the most recent check-in |
| `GET` | `/api/check-ins/{id}` | Find a check-in by ID |
| `POST` | `/api/check-ins` | Create a daily check-in |
| `PUT` | `/api/check-ins/{id}` | Update a daily check-in |
| `DELETE` | `/api/check-ins/{id}` | Delete a daily check-in |

### Recommendations

| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/api/recommendations/latest` | Generate a recommendation from the latest check-in |

## Example

### 1. Create a daily check-in

```http
POST http://localhost:8080/api/check-ins
Content-Type: application/json
```

```json
{
  "id": 1,
  "date": "2026-09-16",
  "cyclePhase": "FOLLICULAR",
  "energyLevel": "HIGH",
  "sleepQuality": "GOOD",
  "symptoms": [],
  "sleepHours": 7.5
}
```

### 2. Request a recommendation

```http
GET http://localhost:8080/api/recommendations/latest
Accept: application/json
```

Example response:

```json
{
  "checkInId": 1,
  "checkInDate": "2026-09-16",
  "workoutType": "TEMPO_RUN",
  "maxPerceivedEffort": 8,
  "reason": "You seem well recovered today. A tempo run could be a good option."
}
```

### 3. Record the completed workout

```http
POST http://localhost:8080/api/workouts
Content-Type: application/json
```

```json
{
  "id": 1,
  "date": "2026-09-16",
  "distanceKm": 8.0,
  "durationMinutes": 48,
  "perceivedEffort": 7,
  "workoutType": "TEMPO_RUN",
  "cyclePhase": "FOLLICULAR"
}
```

The recorded workout does not have to be identical to the recommendation. It
should describe what was actually completed.

## Validation and errors

The API validates requests before they reach the service and persistence
layers.

Some examples of invalid input are:

- a required field is missing;
- a date is in the future;
- distance or duration is not greater than zero;
- perceived effort is outside the range from 1 to 10;
- sleep hours are outside the range from 0 to 24;
- an enum contains an unknown value.

Errors use the same response structure throughout the API:

```json
{
  "timestamp": "2026-09-16T08:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Some request values are invalid. Please review the details below.",
  "path": "/api/workouts",
  "fieldErrors": {
    "distanceKm": "Please enter a distance greater than zero."
  }
}
```

### Status codes

| Status | Meaning |
| --- | --- |
| `200 OK` | A read or update request succeeded |
| `201 Created` | A new record was created |
| `204 No Content` | A record was deleted |
| `400 Bad Request` | The request body or values are invalid |
| `404 Not Found` | A requested resource or required check-in does not exist |
| `409 Conflict` | The selected ID is already in use |
| `500 Internal Server Error` | An unexpected server error occurred |

I tried to keep messages clear and useful without exposing internal exception
details.

## Running the project

### Requirements

- Java 17 or later
- Git

The repository includes the Maven Wrapper, so Maven does not need to be
installed separately.

### Clone the repository

```bash
git clone https://github.com/albapalse/cyclerun-coach.git
cd cyclerun-coach
```

### Start the application

```bash
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

Wait until the terminal displays a message containing:

```text
Started CyclerunCoachApplication
```

The API will be available at:

```text
http://localhost:8080
```

The local profile enables the H2 Console for development. The console is
disabled by default when that profile is not active.

Local application data is stored inside the ignored `data/` directory and is
preserved between restarts.

## Testing

Run all tests with:

```bash
./mvnw test
```

Run a clean build and verification with:

```bash
./mvnw clean verify
```

The test suite contains more than 190 automated tests covering:

- domain validation and behavior;
- recommendation rules and their priority;
- services using in-memory repositories;
- in-memory repository behavior;
- JPA repository adapters;
- web controllers with MockMvc;
- validation and error responses;
- Spring application context startup.

I used different kinds of tests because each layer answers a different
question. Domain and service tests are fast and focused, repository tests check
the persistence mappings, and controller tests verify the HTTP contract.

## Manual testing

Ready-to-run IntelliJ HTTP Client requests are available in:

```text
requests/workouts.http
requests/daily-check-ins.http
requests/recommendations.http
requests/demo.http
```

A more detailed walkthrough is available in the
[user testing guide](docs/user-guide.md).

## Demo flow

For a short demonstration of the project, I use this sequence:

1. Start the application with the demo profile: `SPRING_PROFILES_ACTIVE=demo ./mvnw spring-boot:run`.
2. Create a daily check-in.
3. Request the latest recommendation.
4. Explain which rule produced the recommendation.
5. Record the workout that was actually completed.
6. Retrieve the saved workout and check-in.
7. Send an invalid request and show the `400` response.
8. Request a missing resource and show the `404` response.
9. Create a duplicate ID and show the `409` response.
10. Run the automated tests.

## Decisions I made

### Separate DTOs from domain classes

The controllers use request and response DTOs instead of exposing domain
objects directly. This keeps HTTP validation and API details outside the domain
model.

### Depend on repository interfaces

The services depend on repository interfaces rather than directly on Spring
Data. This lets the application use JPA adapters in the running API and
in-memory implementations in isolated tests.

### Keep JPA entities inside the persistence layer

The domain models are not JPA entities. Dedicated mappers translate between
domain objects and persistence entities.

### Keep recommendations separate from workouts

A suggestion should not be stored as if it were an activity that had already
happened. The application only creates a workout when the client explicitly
records one.

### Use ordered recommendation rules

Some check-in conditions are more important than others. Using an explicit
order makes that priority clear and makes each rule easier to test.

## Current limitations

I kept the scope of the first version deliberately small:

- there are no user accounts or authentication;
- all records belong to one local dataset;
- IDs are assigned manually by the API client;
- H2 is used instead of a production database;
- schema changes are managed by Hibernate rather than migrations;
- list endpoints are not paginated;
- recommendations use fixed rules;
- there is no frontend;
- the API is not publicly deployed.

Because authentication and data ownership are not implemented, shared testing
should use fictional or anonymized data.

## Possible next steps

Some improvements I would like to explore after the first release are:

- authentication and authorization;
- separate data for each user;
- database-generated IDs;
- PostgreSQL;
- Flyway or Liquibase migrations;
- OpenAPI and Swagger documentation;
- pagination and filters;
- Docker support;
- deployment with HTTPS;
- recommendation history;
- recent and weekly training context;
- configurable training goals;
- a web or mobile interface.

## What I learned

This project has helped me understand:

- how an HTTP request moves through a Spring Boot application;
- the responsibilities of controllers, services, and repositories;
- how DTOs protect the API contract and domain model;
- how dependency injection connects application layers;
- the difference between a domain object and a JPA entity;
- how validation errors become HTTP responses;
- how to model and test ordered business rules;
- the purpose of unit, repository, web, and integration tests;
- how to work with branches, pull requests, and incremental changes;
- why removing unused code can make an architecture easier to understand.

There are still parts I want to study in more depth, especially database
decisions, security, and deployment. This project gives me a concrete codebase
I can use for that learning.

## Documentation

- [User testing guide](docs/user-guide.md)
- [Workout HTTP requests](requests/workouts.http)
- [Daily check-in HTTP requests](requests/daily-check-ins.http)
- [Recommendation HTTP request](requests/recommendations.http)
- [Complete demo flow](requests/demo.http)

## License

This project is available under the [MIT License](LICENSE).
