package com.alba.cycleruncoach;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutServiceTest {

    @Test
    void registerWorkout_addsWorkoutToUser() {
        User user = new User("Alba", "123");
        WorkoutService workoutService = new WorkoutService();

        Workout workout = new Workout(111L,
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        workoutService.registerWorkout(user, workout);

        assertEquals(1, user.getWorkouts().size());
        assertTrue(user.getWorkouts().contains(workout));
    }

    @Test
    void registerWorkout_throwsException_whenUserIsNull() {
        WorkoutService workoutService = new WorkoutService();

        Workout workout = new Workout(111L,
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> workoutService.registerWorkout(null, workout)
        );
    }

    @Test
    void calculateTotalDistanceByType_returnsCorrectValue() {
        User user = new User("Alba", "123");
        WorkoutService workoutService = new WorkoutService();

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
        WorkoutService workoutService = new WorkoutService();

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
        WorkoutService workoutService = new WorkoutService();

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
}