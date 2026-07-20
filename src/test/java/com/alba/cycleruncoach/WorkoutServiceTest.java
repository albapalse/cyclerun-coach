package com.alba.cycleruncoach;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutServiceTest {

    @Test
    void calculateTotalDistanceByType_returnsCorrectValue() {
        User user = new User("Alba", "123");
        WorkoutRepository repository = new InMemoryWorkoutRepository();
        WorkoutService workoutService = new WorkoutService(repository);

        Workout workout1 = new Workout(222L,
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        Workout workout2 = new Workout(333L,
                LocalDate.of(2026, 7, 2),
                10.0,
                65,
                8,
                WorkoutType.LONG_RUN,
                CyclePhase.FOLLICULAR
        );

        Workout workout3 = new Workout(444L,
                LocalDate.of(2026, 7, 3),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        user.addWorkout(workout1);
        user.addWorkout(workout2);
        user.addWorkout(workout3);

        double totalDistance = workoutService.calculateTotalDistanceByType(
                user,
                WorkoutType.EASY_RUN
        );

        assertEquals(16.0, totalDistance, 0.001);
    }

    @Test
    void calculateAverageDistanceByCyclePhase_returnsCorrectAverage() {
        User user = new User("Alba", "123");
        WorkoutRepository repository = new InMemoryWorkoutRepository();
        WorkoutService workoutService = new WorkoutService(repository);

        Workout workout1 = new Workout(222L,
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        Workout workout2 = new Workout(333L,
                LocalDate.of(2026, 7, 2),
                10.0,
                65,
                8,
                WorkoutType.LONG_RUN,
                CyclePhase.FOLLICULAR
        );

        Workout workout3 = new Workout(444L,
                LocalDate.of(2026, 7, 3),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        user.addWorkout(workout1);
        user.addWorkout(workout2);
        user.addWorkout(workout3);

        double averageDistance = workoutService.calculateAverageDistanceByCyclePhase(
                user,
                CyclePhase.FOLLICULAR
        );

        assertEquals(8.6666, averageDistance, 0.001);
    }

    @Test
    void calculateAverageDistanceByCyclePhase_returnsZero_whenNoWorkoutsInPhase() {
        User user = new User("Alba", "123");
        WorkoutRepository repository = new InMemoryWorkoutRepository();
        WorkoutService workoutService = new WorkoutService(repository);

        Workout workout1 = new Workout(555L,
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        Workout workout2 = new Workout(666L,
                LocalDate.of(2026, 7, 2),
                10.0,
                65,
                8,
                WorkoutType.LONG_RUN,
                CyclePhase.FOLLICULAR
        );

        Workout workout3 = new Workout(777L,
                LocalDate.of(2026, 7, 3),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        user.addWorkout(workout1);
        user.addWorkout(workout2);
        user.addWorkout(workout3);

        double averageDistance = workoutService.calculateAverageDistanceByCyclePhase(
                user,
                CyclePhase.MENSTRUAL
        );

        assertEquals(0.0, averageDistance, 0.001);
    }

    @Test
    void constructor_throwsException_whenRepositoryIsNull() {
      assertThrows(IllegalArgumentException.class, () -> new WorkoutService(null));
    }
    @Test
    void saveWorkout_savesWorkoutInRepository() {
        WorkoutRepository workoutRepository = new InMemoryWorkoutRepository();
        WorkoutService workoutService = new WorkoutService(workoutRepository);
        Workout workout = new Workout(777L,
                LocalDate.of(2026, 7, 3),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        workoutService.saveWorkout(workout);

        assertEquals(workoutRepository.findAll().get(0), workout);
    }
    @Test
    void findAllWorkouts_returnsSavedWorkouts() {
        WorkoutRepository workoutRepository = new InMemoryWorkoutRepository();
        WorkoutService workoutService = new WorkoutService(workoutRepository);
        Workout workout = new Workout(777L,
                LocalDate.of(2026, 7, 3),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        Workout workout2 = new Workout(666L,
                LocalDate.of(2026, 7, 2),
                10.0,
                65,
                8,
                WorkoutType.LONG_RUN,
                CyclePhase.FOLLICULAR
        );
        workoutService.saveWorkout(workout);
        workoutService.saveWorkout(workout2);
        List<Workout> workouts = workoutService.findAllWorkouts();
        assertEquals(workouts.size(), 2);

    }

    @Test
    void findWorkoutById_returnsWorkout_whenWorkoutExists(){
        WorkoutRepository workoutRepository = new InMemoryWorkoutRepository();
        WorkoutService workoutService = new WorkoutService(workoutRepository);
        Workout workout = new Workout(777L,
                LocalDate.of(2026, 7, 3),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        Workout workout2 = new Workout(666L,
                LocalDate.of(2026, 7, 2),
                10.0,
                65,
                8,
                WorkoutType.LONG_RUN,
                CyclePhase.FOLLICULAR
        );
        workoutService.saveWorkout(workout);
        workoutService.saveWorkout(workout2);
        Workout foundWorkout = workoutService.findWorkoutById(777L);
        assertEquals(workout, foundWorkout);

    }

    @Test
    void findWorkoutById_returnsNull_whenWorkoutDoesNotExist() {
        WorkoutRepository workoutRepository = new InMemoryWorkoutRepository();
        WorkoutService workoutService = new WorkoutService(workoutRepository);

        Workout workout = new Workout(666L,
                LocalDate.of(2026, 7, 2),
                10.0,
                65,
                8,
                WorkoutType.LONG_RUN,
                CyclePhase.FOLLICULAR
        );
        workoutService.saveWorkout(workout);
        Workout foundWorkout = workoutService.findWorkoutById(777L);
        assertNull(foundWorkout);

    }

    @Test
    void deleteWorkoutById_deletesWorkout_whenWorkoutExists() {
        WorkoutRepository workoutRepository = new InMemoryWorkoutRepository();
        WorkoutService workoutService = new WorkoutService(workoutRepository);
        Workout workout = new Workout(777L,
                LocalDate.of(2026, 7, 3),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        workoutService.saveWorkout(workout);

        assertTrue( workoutService.deleteWorkoutById(777L));

    }

    @Test
    void deleteWorkoutById_returnsFalse_whenWorkoutDoesNotExist() {
        WorkoutRepository workoutRepository = new InMemoryWorkoutRepository();
        WorkoutService workoutService = new WorkoutService(workoutRepository);

        assertFalse(workoutService.deleteWorkoutById(777L));

    }
}