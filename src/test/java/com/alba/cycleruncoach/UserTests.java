package com.alba.cycleruncoach;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructor_createsUserWithValidData() {
        User user = new User("Alba", "123");

        assertEquals("Alba", user.getUsername());
        assertTrue(user.getWorkouts().isEmpty());
    }

    @Test
    void constructor_throwsException_whenUsernameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(null, "123");
        });
    }

    @Test
    void constructor_throwsException_whenUsernameIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(" ", "123");
        });
    }

    @Test
    void constructor_throwsException_whenPasswordIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("Alba", null);
        });
    }

    @Test
    void constructor_throwsException_whenPasswordIsBlank() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("Alba", " ");
        });
    }

    @Test
    void addWorkout_addsWorkoutToUser() {
        User user = new User("Alba", "123");
        Workout workout = new Workout(LocalDate.of(2026, 7, 1), 10.0, 50, 6);

        user.addWorkout(workout);

        assertEquals(1, user.getWorkouts().size());
        assertTrue(user.getWorkouts().contains(workout));
    }

    @Test
    void addWorkout_throwsException_whenWorkoutIsNull() {
        User user = new User("Alba", "123");

        assertThrows(IllegalArgumentException.class, () -> {
            user.addWorkout(null);
        });
    }

    @Test
    void addWorkout_throwsException_whenWorkoutAlreadyExists() {
        User user = new User("Alba", "123");
        Workout workout = new Workout(LocalDate.of(2026, 7, 1), 10.0, 50, 6);

        user.addWorkout(workout);

        assertThrows(IllegalArgumentException.class, () -> {
            user.addWorkout(workout);
        });
    }

    @Test
    void removeWorkout_removesExistingWorkout() {
        User user = new User("Alba", "123");
        Workout workout = new Workout(LocalDate.of(2026, 7, 1), 10.0, 50, 6);

        user.addWorkout(workout);

        boolean removed = user.removeWorkout(workout);

        assertTrue(removed);
        assertFalse(user.getWorkouts().contains(workout));
        assertTrue(user.getWorkouts().isEmpty());
    }

    @Test
    void removeWorkout_returnsFalse_whenWorkoutDoesNotExist() {
        User user = new User("Alba", "123");
        Workout workout = new Workout(LocalDate.of(2026, 7, 1), 10.0, 50, 6);

        boolean removed = user.removeWorkout(workout);

        assertFalse(removed);
        assertTrue(user.getWorkouts().isEmpty());
    }

    @Test
    void calculateTotalKms_returnsCorrectKms() {
        User user = new User("Alba", "123");

        Workout workout1 = new Workout(LocalDate.of(2026, 7, 1), 10.0, 50, 6);
        Workout workout2 = new Workout(LocalDate.of(2026, 7, 2), 12.0, 60, 5);

        user.addWorkout(workout1);
        user.addWorkout(workout2);

        double kms = user.calculateTotalKms();

        assertEquals(22.0, kms, 0.001);
    }

    @Test
    void getWorkouts_returnsCopyOfInternalList() {
        User user = new User("Alba", "123");
        Workout workout = new Workout(LocalDate.of(2026, 7, 1), 10.0, 50, 6);

        user.addWorkout(workout);

        List<Workout> workoutsCopy = user.getWorkouts();
        workoutsCopy.clear();

        assertEquals(1, user.getWorkouts().size());
    }
}