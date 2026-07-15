package com.alba.cycleruncoach;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class InMemoryWorkoutRepositoryTest {
    @Test
        void save_saveWorkout() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();
        User user = new User("Alba", "123");
        Workout workout = new Workout(111L,
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
    void findAll_returnWorkouts() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();
        User user = new User("Alba", "123");
        Workout workout1 = new Workout(111L,
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        Workout workout2 = new Workout(112L,
                LocalDate.of(2026, 7, 2),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        repository.save(workout1);
        repository.save(workout2);

        List<Workout> workouts = repository.findAll();
        assertTrue(workouts.contains(workout1));
        assertTrue(workouts.contains(workout2));
        assertEquals(2, workouts.size());

    }

    @Test
    void findById_returnWorkout() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();
        User user = new User("Alba", "123");
        Workout workout1 = new Workout(111L,
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        Workout workout2 = new Workout(112L,
                LocalDate.of(2026, 7, 2),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        repository.save(workout1);
        repository.save(workout2);

        Workout workout = repository.findById(111L);

        assertEquals(workout1, workout);

    }
    @Test
    void findById_retursNull_whenWorkoutDoesNotExist() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();
        User user = new User("Alba", "123");
        Workout workout = repository.findById(111L);

        assertNull(workout);

    }

    @Test
    void deleteById_returnTrue() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();
        User user = new User("Alba", "123");
        Workout workout1 = new Workout(111L,
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        Workout workout2 = new Workout(112L,
                LocalDate.of(2026, 7, 2),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        repository.save(workout1);
        repository.save(workout2);
        assertTrue(repository.deleteById(111L));

    }
    @Test
    void deleteById_returnsFalse_whenWorkoutDoesNotExist() {
        InMemoryWorkoutRepository repository = new InMemoryWorkoutRepository();

        boolean deleted = repository.deleteById(99L);

        assertFalse(deleted);
    }

}
