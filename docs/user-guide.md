# CycleRun Coach — User Testing Guide

Hi! I built CycleRun Coach to explore how running, recovery, energy, symptoms,
and menstrual cycle information can be considered together.

Thank you for helping me test it. Your feedback will help me understand what
feels useful, what is confusing, and what I should improve next.

## What CycleRun Coach does

CycleRun Coach is currently a REST API rather than a finished mobile or web
application. It can:

- save completed running workouts;
- save daily wellness and cycle check-ins;
- retrieve, update, and delete saved records;
- identify the most recent daily check-in;
- suggest a training session based on the most recent check-in;
- explain why a particular training option was suggested.

The recommendation rules consider reported symptoms, energy, sleep, and cycle
phase. Recovery-related information is given priority so that recommendations
remain conservative.

Recommendations are general training suggestions. They are not medical advice
and should not replace professional medical guidance or your own judgment
about how you feel.

## Current testing limitations

This version does not yet have user accounts or authentication. Do not submit
your name, email address, medical history, or any other identifying information.

When testing a shared version, please use fictional or anonymized data. Personal
training data should only be used while running the API privately on your own
computer.

The API currently uses manually assigned IDs. Every new workout and daily
check-in must have a unique positive ID such as `1`, `2`, or `3`.

## What you need

For local testing, you need:

- Java 17 or later;
- the CycleRun Coach project;
- a terminal;
- an HTTP client such as Postman, Insomnia, IntelliJ HTTP Client, or the REST
  Client extension for Visual Studio Code.

The examples in this guide use:

```text
http://localhost:8080
```

If I give you a hosted testing URL, replace `http://localhost:8080` with that
URL.

## Start the API locally

Open a terminal in the project directory and run:

```bash
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

Wait until the terminal displays a message containing:

```text
Started CyclerunCoachApplication
```

Keep that terminal open while testing.

To confirm that the API is working, send:

```http
GET http://localhost:8080/api/workouts
Accept: application/json
```

A new database should return:

```json
[]
```

## Suggested first test

A simple testing flow is:

1. Create today's daily check-in.
2. Request a training recommendation.
3. Review whether the recommendation feels appropriate.
4. Complete a training session if you choose to.
5. Save the session as a workout afterward.
6. List the saved check-ins and workouts.
7. Correct or delete any test records if necessary.

## Create a daily check-in

A daily check-in describes how you feel today. Send:

```http
POST http://localhost:8080/api/check-ins
Content-Type: application/json
```

Example body:

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

Use today's date or an earlier date in `YYYY-MM-DD` format.

If you have no symptoms, send an empty list:

```json
"symptoms": []
```

### Accepted check-in values

| Field | Accepted values |
| --- | --- |
| `id` | A unique positive whole number |
| `date` | Today or an earlier date in `YYYY-MM-DD` format |
| `cyclePhase` | `MENSTRUAL`, `FOLLICULAR`, `OVULATORY`, `LUTEAL` |
| `energyLevel` | `EXHAUSTED`, `LOW`, `MEDIUM`, `HIGH`, `VERY_HIGH` |
| `sleepQuality` | `BAD`, `OK`, `GOOD`, `EXCELLENT` |
| `symptoms` | `CRAMPS`, `FATIGUE`, `HEADACHE`, `BLOATING`, `STOMACH_ACHE`, `MUSCLE_SORENESS` |
| `sleepHours` | A number from `0` to `24` |

## Get a training recommendation

First, create at least one daily check-in. Then send:

```http
GET http://localhost:8080/api/recommendations/latest
Accept: application/json
```

The API finds the most recent check-in by date and returns:

- the ID and date of the check-in it used;
- a suggested workout type;
- a maximum perceived effort;
- a short explanation.

Example response:

```json
{
  "checkInId": 1,
  "checkInDate": "2026-09-16",
  "workoutType": "EASY_RUN",
  "maxPerceivedEffort": 4,
  "reason": "You reported symptoms today. Consider an easy run and adjust the effort based on how you feel."
}
```

A recommendation is only a suggestion for the next training session. It does
not create or save a completed workout.

If you decide to follow the recommendation, save what you actually completed
afterward using `POST /api/workouts`. The completed workout may differ from the
recommendation.

If no daily check-in exists yet, the API returns `404 Not Found` with a message
explaining that a check-in must be added first.

## How recommendations are selected

Recovery-related conditions have priority over cycle phase.

Examples include:

- exhausted energy may result in a recovery run with very low effort;
- fewer than five hours of sleep may result in a recovery run;
- reported symptoms may result in an easy run with effort limited to `4`;
- fewer than seven hours of sleep may reduce the suggested intensity;
- favorable readiness may allow training based on cycle phase;
- uncertain or balanced readiness defaults to a conservative easy run.

If several conditions apply, the more conservative rule takes priority.

Always adjust or skip the suggested session if it does not feel appropriate.

## Create a completed workout

A workout represents a running session that has already happened. It is
different from a recommendation, which is only a suggestion.

Send:

```http
POST http://localhost:8080/api/workouts
Content-Type: application/json
```

Example body:

```json
{
  "id": 1,
  "date": "2026-09-16",
  "distanceKm": 8.0,
  "durationMinutes": 48,
  "perceivedEffort": 5,
  "workoutType": "EASY_RUN",
  "cyclePhase": "FOLLICULAR"
}
```

### Accepted workout values

| Field | Accepted values |
| --- | --- |
| `id` | A unique positive whole number |
| `date` | Today or an earlier date in `YYYY-MM-DD` format |
| `distanceKm` | A number greater than `0` |
| `durationMinutes` | A whole number greater than `0` |
| `perceivedEffort` | A whole number from `1` to `10` |
| `workoutType` | `EASY_RUN`, `RECOVERY_RUN`, `TEMPO_RUN`, `INTERVALS`, `LONG_RUN` |
| `cyclePhase` | `MENSTRUAL`, `FOLLICULAR`, `OVULATORY`, `LUTEAL` |

Perceived effort is personal. A simple guide is:

- `1–3`: very easy;
- `4–6`: comfortable to moderate;
- `7–8`: hard;
- `9–10`: maximal or almost maximal.

## Read saved data

| Action | Method and path |
| --- | --- |
| List workouts | `GET /api/workouts` |
| Find workout by ID | `GET /api/workouts/{id}` |
| List daily check-ins | `GET /api/check-ins` |
| Find daily check-in by ID | `GET /api/check-ins/{id}` |
| Find latest daily check-in | `GET /api/check-ins/latest` |
| Get latest recommendation | `GET /api/recommendations/latest` |

For example:

```http
GET http://localhost:8080/api/check-ins/latest
Accept: application/json
```

Replace `{id}` with a real record ID. For example:

```http
GET http://localhost:8080/api/workouts/1
Accept: application/json
```

## Correct saved data

Use `PUT` with the record ID in the URL. The update body does not include the
`id` because the ID is already part of the URL.

### Update a workout

```http
PUT http://localhost:8080/api/workouts/1
Content-Type: application/json

{
  "date": "2026-09-16",
  "distanceKm": 8.2,
  "durationMinutes": 49,
  "perceivedEffort": 5,
  "workoutType": "EASY_RUN",
  "cyclePhase": "FOLLICULAR"
}
```

### Update a daily check-in

```http
PUT http://localhost:8080/api/check-ins/1
Content-Type: application/json

{
  "date": "2026-09-16",
  "cyclePhase": "FOLLICULAR",
  "energyLevel": "MEDIUM",
  "sleepQuality": "GOOD",
  "symptoms": ["FATIGUE"],
  "sleepHours": 7.0
}
```

After updating the most recent check-in, request the recommendation again to
see whether it changes.

## Delete test data

Delete a workout:

```http
DELETE http://localhost:8080/api/workouts/1
```

Delete a daily check-in:

```http
DELETE http://localhost:8080/api/check-ins/1
```

A successful deletion returns `204 No Content`.

Deleting a check-in does not delete workouts, and deleting a workout does not
delete check-ins.

## Understanding responses

Common successful status codes are:

- `200 OK`: the request worked;
- `201 Created`: a new record was created;
- `204 No Content`: a record was deleted.

Common error status codes are:

- `400 Bad Request`: one or more values are missing or invalid;
- `404 Not Found`: the requested record or required check-in does not exist;
- `409 Conflict`: the selected ID is already in use;
- `500 Internal Server Error`: the API encountered an unexpected problem.

Error responses include:

- the exact time of the error;
- the HTTP status code;
- a short error name;
- a clear message;
- the requested path;
- individual field messages when validation fails.

Example validation error:

```json
{
  "timestamp": "2026-09-16T08:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Some request fields are invalid. Please review the field errors.",
  "path": "/api/workouts",
  "fieldErrors": {
    "distanceKm": "Distance must be greater than zero"
  }
}
```

Example response when no check-in is available for a recommendation:

```json
{
  "timestamp": "2026-09-16T08:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "No daily check-in was found yet. Add today's check-in first, and then we'll prepare your recommendation.",
  "path": "/api/recommendations/latest",
  "fieldErrors": {}
}
```

## Helpful feedback

While testing, please let me know:

- whether the instructions and API messages are easy to understand;
- whether the recommendation feels appropriate for the submitted check-in;
- whether the suggested maximum effort feels reasonable;
- whether any accepted values or fields are confusing;
- whether you encounter an unexpected status code or response;
- what you would find most useful in a future web or mobile interface.

When reporting a problem, please include:

1. the request method and URL;
2. the JSON request body, if one was used;
3. the response status;
4. the response body;
5. a short explanation of what you expected.

Do not include personal or identifying information in a problem report.

## Stop the API

When you finish testing, return to the terminal running the application and
press `Control + C`.

Thank you for helping me test CycleRun Coach!