# CycleRun Coach

CycleRun Coach is a Java Spring Boot API for managing running workouts with a cycle-aware approach.

The idea behind this project is to help women track their running training together with factors such as energy levels, menstrual cycle phase, symptoms, and recovery. Based on this information, the API will provide simple workout recommendations.

## Project Status

This project is currently in early development. Workout and daily check-in
tracking are available through REST APIs and persist in a local H2 database.
Training recommendations are still planned.

## Current Features

- Create, read, update, and delete running workouts
- Track workout details such as date, distance, duration, intensity, and workout type
- Create, read, update, and delete daily cycle check-ins
- Track energy levels, symptoms, and sleep quality
- Retrieve the latest daily check-in
- Preserve workouts, check-ins, and symptoms after restarting the application

Planned next feature:

- Generate basic training recommendations based on cycle phase and energy level

## Tech Stack

- Java 17
- Spring Boot
- Maven
- Spring Web
- Spring Data JPA
- H2 Database
- Jakarta Validation
- JUnit 5
- Git and GitHub

## Learning Goals

This project is part of my learning path as a Computer Engineering student.

Through this project, I want to practice building a backend application using Java and Spring Boot. My main goals are to improve my understanding of:

- Object-oriented programming in Java
- REST API design
- Layered architecture
- Spring Boot fundamentals
- Database persistence
- Input validation
- Unit and integration testing
- Git and GitHub workflow

Database decisions that still need deeper study are tracked in the
[database learning backlog](docs/database-learning-backlog.md).

## Current Architecture

The domain model is independent from persistence details. Controllers and
services depend on repository interfaces; JPA adapters implement those
interfaces and translate between domain objects and database entities.

```text
HTTP/JSON
   -> Controller
   -> Service
   -> Repository interface
   -> JPA repository adapter
   -> Mapper
   -> JPA entity / Spring Data
   -> H2
```

The in-memory repository implementations remain available for isolated unit
tests, but production wiring uses the JPA adapters.
