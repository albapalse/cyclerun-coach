package com.alba.cycleruncoach.repository.jpa;

import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.Workout;
import com.alba.cycleruncoach.domain.WorkoutType;
import com.alba.cycleruncoach.repository.WorkoutRepository;
import com.alba.cycleruncoach.exception.DuplicateResourceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(JpaWorkoutRepositoryAdapter.class)
class JpaWorkoutRepositoryAdapterTest {

    @Autowired
    private WorkoutRepository repository;

    @Test
    void shouldSaveAndFindWorkoutById() {
        Workout workout = createWorkout(1L);

        repository.save(workout);

        Workout storedWorkout = repository.findById(1L).orElseThrow();

        assertEquals(1L, storedWorkout.getId());
        assertEquals(workout, storedWorkout);
    }

    @Test
    void shouldFindAllWorkouts() {
        Workout firstWorkout = createWorkout(1L);
        Workout secondWorkout = new Workout(
                2L,
                LocalDate.of(2026, 9, 10),
                6.0,
                35,
                8,
                WorkoutType.INTERVALS,
                CyclePhase.LUTEAL
        );

        repository.save(firstWorkout);
        repository.save(secondWorkout);

        List<Workout> workouts = repository.findAll();

        assertEquals(2, workouts.size());
        assertTrue(workouts.contains(firstWorkout));
        assertTrue(workouts.contains(secondWorkout));
    }

    @Test
    void shouldReturnEmptyWhenWorkoutDoesNotExist() {
        assertTrue(repository.findById(999L).isEmpty());
    }

    @Test
    void shouldRejectDuplicateId() {
        repository.save(createWorkout(1L));

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> repository.save(createWorkout(1L))
        );

        assertEquals(
                "Workout with id 1 already exists",
                exception.getMessage()
        );
    }

    @Test
    void shouldUpdateExistingWorkout() {
        repository.save(createWorkout(1L));

        Workout updatedWorkout = new Workout(
                1L,
                LocalDate.of(2026, 9, 11),
                12.0,
                65,
                7,
                WorkoutType.TEMPO_RUN,
                CyclePhase.LUTEAL
        );

        boolean updated = repository.update(updatedWorkout);
        Workout storedWorkout = repository.findById(1L).orElseThrow();

        assertTrue(updated);
        assertEquals(updatedWorkout, storedWorkout);
        assertEquals(1L, storedWorkout.getId());
    }

    @Test
    void shouldReturnFalseWhenUpdatingMissingWorkout() {
        boolean updated = repository.update(createWorkout(99L));

        assertFalse(updated);
        assertTrue(repository.findById(99L).isEmpty());
    }

    @Test
    void shouldDeleteExistingWorkout() {
        repository.save(createWorkout(1L));

        boolean deleted = repository.deleteById(1L);

        assertTrue(deleted);
        assertTrue(repository.findById(1L).isEmpty());
    }

    @Test
    void shouldReturnFalseWhenDeletingMissingWorkout() {
        boolean deleted = repository.deleteById(99L);

        assertFalse(deleted);
    }

    @Test
    void shouldRejectNullWorkout() {
        assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.update(null)
        );
    }

    @Test
    void shouldRejectInvalidIds() {
        assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById(null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById(0L)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> repository.deleteById(-1L)
        );
    }

    private Workout createWorkout(Long id) {
        return new Workout(
                id,
                LocalDate.of(2026, 9, 9),
                8.5,
                48,
                6,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
    }
}