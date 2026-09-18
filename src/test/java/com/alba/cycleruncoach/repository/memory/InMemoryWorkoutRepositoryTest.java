package com.alba.cycleruncoach.repository.memory;

import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.Workout;
import com.alba.cycleruncoach.domain.WorkoutType;
import com.alba.cycleruncoach.exception.DuplicateResourceException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

        Optional<Workout> workout = repository.findById(111L);

        assertTrue(workout.isPresent());
        assertEquals(firstWorkout, workout.orElseThrow());
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
    void findById_returnsEmpty_whenWorkoutDoesNotExist() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();

        Optional<Workout> workout = repository.findById(111L);

        assertTrue(workout.isEmpty());
    }

    @Test
    void update_replacesWorkout_whenWorkoutExists() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();
        Workout originalWorkout = new Workout(
                111L,
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                5,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        Workout updatedWorkout = new Workout(
                111L,
                LocalDate.of(2026, 7, 1),
                10.0,
                55,
                8,
                WorkoutType.TEMPO_RUN,
                CyclePhase.LUTEAL
        );
        repository.save(originalWorkout);

        boolean updated = repository.update(updatedWorkout);

        assertTrue(updated);
        assertEquals(
                updatedWorkout,
                repository.findById(111L).orElseThrow()
        );
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void update_returnsFalse_whenWorkoutDoesNotExist() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();
        Workout workout = new Workout(
                111L,
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                5,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        boolean updated = repository.update(workout);

        assertFalse(updated);
        assertTrue(repository.findAll().isEmpty());
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

    @Test
    void save_throwsException_whenWorkoutIsNull() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();

        assertThrows(IllegalArgumentException.class, () -> repository.save(null));
    }

    @Test
    void save_throwsException_whenIdAlreadyExists() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();
        Workout firstWorkout = createWorkout(111L);
        Workout duplicateWorkout = createWorkout(111L);
        repository.save(firstWorkout);

        assertThrows(
                DuplicateResourceException.class,
                () -> repository.save(duplicateWorkout)
        );
    }

    @Test
    void update_throwsException_whenWorkoutIsNull() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();

        assertThrows(IllegalArgumentException.class, () -> repository.update(null));
    }

    @Test
    void deleteById_throwsException_whenIdIsInvalid() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();

        assertThrows(IllegalArgumentException.class, () -> repository.deleteById(null));
        assertThrows(IllegalArgumentException.class, () -> repository.deleteById(0L));
        assertThrows(IllegalArgumentException.class, () -> repository.deleteById(-1L));
    }

    @Test
    void findAll_returnsCopyOfInternalCollection() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();
        Workout workout = createWorkout(111L);
        repository.save(workout);

        List<Workout> result = repository.findAll();
        result.clear();

        assertEquals(List.of(workout), repository.findAll());
    }

    private Workout createWorkout(Long id) {
        return new Workout(
                id,
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
    }
}
