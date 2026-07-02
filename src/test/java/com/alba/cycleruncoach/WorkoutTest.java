package com.alba.cycleruncoach;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WorkoutTest {

    @Test
    void constructor_createsWorkoutWithValidData() {
        Workout workout = new Workout(
                LocalDate.of(2026, 7, 1),
                5.0,
                30,
                3
        );

        assertEquals(LocalDate.of(2026, 7, 1), workout.getDate());
        assertEquals(5.0, workout.getDistanceKm(), 0.001);
        assertEquals(30, workout.getDurationMinutes());
        assertEquals(3, workout.getPerceivedEffort());
    }

    @Test
    void calculatePace_returnsMinutesPerKilometer() {
        Workout workout = new Workout(
                LocalDate.of(2026, 7, 1),
                10.0,
                60,
                5
        );

        double pace = workout.calculatePace();

        assertEquals(6.0, pace, 0.001);
    }

    @Test
    void calculateTrainingLoad_returnsDistanceMultipliedByEffort() {
        Workout workout = new Workout(
                LocalDate.of(2026, 7, 1),
                10.0,
                60,
                5
        );

        int trainingLoad = workout.calculateTrainingLoad();

        assertEquals(50, trainingLoad);
    }

    @Test
    void constructor_throwsException_whenDateIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(null, 10.0, 60, 5);
        });
    }

    @Test
    void constructor_throwsException_whenDistanceIsZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(LocalDate.of(2026, 7, 1), 0.0, 60, 5);
        });
    }

    @Test
    void constructor_throwsException_whenDistanceIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(LocalDate.of(2026, 7, 1), -10.0, 60, 5);
        });
    }

    @Test
    void constructor_throwsException_whenDurationIsZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(LocalDate.of(2026, 7, 1), 10.0, 0, 5);
        });
    }

    @Test
    void constructor_throwsException_whenDurationIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(LocalDate.of(2026, 7, 1), 10.0, -60, 5);
        });
    }

    @Test
    void constructor_throwsException_whenPerceivedEffortIsLowerThanOne() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(LocalDate.of(2026, 7, 1), 10.0, 60, 0);
        });
    }

    @Test
    void constructor_throwsException_whenPerceivedEffortIsGreaterThanTen() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(LocalDate.of(2026, 7, 1), 10.0, 60, 11);
        });
    }
}