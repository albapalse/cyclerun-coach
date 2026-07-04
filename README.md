# CycleRun Coach

CycleRun Coach is a Java Spring Boot API for managing running workouts with a cycle-aware approach.

The idea behind this project is to help women track their running training together with factors such as energy levels, menstrual cycle phase, symptoms, and recovery. Based on this information, the API will provide simple workout recommendations.

## Project Status

This project is currently in early development.

The first version will focus on building a clean backend API with basic workout tracking, cycle logs, and simple training recommendations.

## Main Features

Planned features:

- Create, read, update, and delete running workouts
- Track workout details such as date, distance, duration, intensity, and workout type
- Register daily cycle information
- Track energy levels, symptoms, and sleep quality
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

## Planned Architecture

The project will follow a layered structure:

```text
Controller  -> handles HTTP requests
Service     -> contains business logic
Repository  -> communicates with the database
Entity      -> represents database tables
DTO         -> transfers data between the API and the client