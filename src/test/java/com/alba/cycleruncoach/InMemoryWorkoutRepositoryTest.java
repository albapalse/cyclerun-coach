package com.alba.cycleruncoach;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InMemoryWorkoutRepositoryTest {

    @Test
    void save_storesWorkout() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();
        Workout workout = new Workout(
                111L,
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        repository.save(workout);

        assertTrue(repository.findAll().contains(workout));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void findAll_returnsSavedWorkouts() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();
        Workout firstWorkout = new Workout(
                111L,
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        Workout secondWorkout = new Workout(
                112L,
                LocalDate.of(2026, 7, 2),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        repository.save(firstWorkout);
        repository.save(secondWorkout);

        List<Workout> workouts = repository.findAll();

        assertTrue(workouts.contains(firstWorkout));
        assertTrue(workouts.contains(secondWorkout));
        assertEquals(2, workouts.size());
    }

    @Test
    void findById_returnsWorkout_whenWorkoutExists() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();
        Workout firstWorkout = new Workout(
                111L,
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        Workout secondWorkout = new Workout(
                112L,
                LocalDate.of(2026, 7, 2),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        repository.save(firstWorkout);
        repository.save(secondWorkout);

        Workout workout = repository.findById(111L);

        assertEquals(firstWorkout, workout);
    }

    @Test
    void findById_throwsException_whenIdIsZero() {
        InMemoryWorkoutRepository repository =
                new InMemoryWorkoutRepository();

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById(0L)
        );
    }

    @Test
    void findById_throwsException_whenIdIsNull() {
        InMemoryWorkoutRepository repository =
                new InMemoryWorkoutRepository();

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById(null)
        );
    }

    @Test
    void findById_throwsException_whenIdIsNegative() {
        InMemoryWorkoutRepository repository =
                new InMemoryWorkoutRepository();

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById(-1L)
        );
    }

    @Test
    void findById_returnsNull_whenWorkoutDoesNotExist() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();

        Workout workout = repository.findById(111L);

        assertNull(workout);
    }

    @Test
    void deleteById_returnsTrue_whenWorkoutExists() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();
        Workout firstWorkout = new Workout(
                111L,
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        Workout secondWorkout = new Workout(
                112L,
                LocalDate.of(2026, 7, 2),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        repository.save(firstWorkout);
        repository.save(secondWorkout);

        boolean deleted = repository.deleteById(111L);

        assertTrue(deleted);
    }
    @Test
    void deleteById_returnsFalse_whenWorkoutDoesNotExist() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();

        boolean deleted = repository.deleteById(99L);

        assertFalse(deleted);
    }
}
