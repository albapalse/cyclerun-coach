# Database learning backlog

## Current scope

- Workouts and daily check-ins use Spring Data JPA adapters.
- Both resources are stored in the same file-based H2 database.
- Daily check-in symptoms are stored in a separate collection table as enum names.
- The current setup is suitable for local development and learning on one application instance.
- Tests use an in-memory H2 database and never depend on local development data.

## Questions to revisit

- Where should transaction boundaries live when one use case updates several aggregates?
- How should duplicate identifiers be handled safely under concurrent requests?
- Which database constraints should also enforce the domain invariants?
- When should manual identifiers be replaced with database-generated identifiers?
- Which indexes will be useful for searches by date and for finding the latest check-in?
- How should schema changes be versioned with Flyway instead of `ddl-auto=update`?
- What changes are required to move from local H2 to PostgreSQL?
- When should symptoms become their own entity instead of an element collection?
