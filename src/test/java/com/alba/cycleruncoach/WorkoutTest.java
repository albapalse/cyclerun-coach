package com.alba.cycleruncoach;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutTest {

    @Test
    void constructor_createsWorkoutWithValidData() {
        Workout workout = new Workout(111L,
                LocalDate.of(2026, 7, 1),
                5.0,
                30,
                3, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR
        );
        assertEquals(111, workout.getId());
        assertEquals(LocalDate.of(2026, 7, 1), workout.getDate());
        assertEquals(5.0, workout.getDistanceKm(), 0.001);
        assertEquals(30, workout.getDurationMinutes());
        assertEquals(3, workout.getPerceivedEffort());
        assertEquals(CyclePhase.FOLLICULAR, workout.getCyclePhase());
        assertEquals(WorkoutType.INTERVALS, workout.getWorkoutType());
    }

    @Test
    void calculatePace_returnsMinutesPerKilometer() {
        Workout workout = new Workout(111L,
                LocalDate.of(2026, 7, 1),
                10.0,
                60,
                5, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR
        );

        double pace = workout.calculatePace();

        assertEquals(6.0, pace, 0.001);
    }

    @Test
    void calculateTrainingLoad_returnsDistanceMultipliedByEffort() {
        Workout workout = new Workout(111L,
                LocalDate.of(2026, 7, 1),
                10.0,
                60,
                5, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR
        );

        int trainingLoad = workout.calculateTrainingLoad();

        assertEquals(50, trainingLoad);
    }

    @Test
    void isHighIntensity_returnsTrue() {
        Workout workout = new Workout(111L, LocalDate.of(2026, 7, 1), 10, 60, 8, WorkoutType.LONG_RUN, CyclePhase.MENSTRUAL);
        assertTrue(workout.isHighIntensity());
    }

    @Test
    void isHighIntensity_returnsFalse() {
        Workout workout = new Workout(111L, LocalDate.of(2026, 7, 1), 10, 60, 5, WorkoutType.EASY_RUN, CyclePhase.MENSTRUAL);
        assertFalse(workout.isHighIntensity());
    }

    @Test
    void constructor_createsWorkoutWithTypeAndCyclePhase() {
        Workout workout = new Workout(111L,
                LocalDate.of(2026, 7, 1),
                10.0,
                60,
                5,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        assertEquals(WorkoutType.EASY_RUN, workout.getWorkoutType());
        assertEquals(CyclePhase.FOLLICULAR, workout.getCyclePhase());
    }

    @Test
    void constructor_throwsException_whenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(
                    null,
                    LocalDate.of(2025, 7, 1),
                    10.0,
                    60,
                    5,
                    WorkoutType.INTERVALS,
                    CyclePhase.FOLLICULAR
            );
        });
    }

    @Test
    void constructor_throwsException_whenIdIsZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(
                    0L,
                    LocalDate.of(2025, 7, 1),
                    10.0,
                    60,
                    5,
                    WorkoutType.INTERVALS,
                    CyclePhase.FOLLICULAR
            );
        });
    }

    @Test
    void constructor_throwsException_whenIdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(
                    -1L,
                    LocalDate.of(2025, 7, 1),
                    10.0,
                    60,
                    5,
                    WorkoutType.INTERVALS,
                    CyclePhase.FOLLICULAR
            );
        });
    }


    @Test
    void constructor_throwsException_whenDateIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(111L,null, 10.0, 60, 5, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);
        });
    }

    @Test
    void constructor_throwsException_whenDistanceIsZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(111L, LocalDate.of(2026, 7, 1), 0.0, 60, 5,  WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);
        });
    }

    @Test
    void constructor_throwsException_whenDistanceIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(111L, LocalDate.of(2026, 7, 1), -10.0, 60, 5,  WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);
        });
    }

    @Test
    void constructor_throwsException_whenDurationIsZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(111L, LocalDate.of( 2026, 7, 1), 10.0, 0, 5, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);
        });
    }

    @Test
    void constructor_throwsException_whenDurationIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(111L, LocalDate.of(2026, 7, 1), 10.0, -60, 5,   WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);
        });
    }

    @Test
    void constructor_throwsException_whenPerceivedEffortIsLowerThanOne() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(111L, LocalDate.of(2026, 7, 1), 10.0, 60, 0,WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);
        });
    }

    @Test
    void constructor_throwsException_whenPerceivedEffortIsGreaterThanTen() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(111L, LocalDate.of(2026, 7, 1), 10.0, 60, 11, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);
        });
    }

    @Test
    void constructor_throwsException_whenWorkoutTypeIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(111L, LocalDate.of(2026,7,1), 10.0, 60, 5, null, CyclePhase.FOLLICULAR);
        });

    }

    @Test
    void constructor_throwsException_whenCyclePhaseIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Workout(111L, LocalDate.of(2026,7,1), 10.0, 60, 5, WorkoutType.INTERVALS, null);
        });

    }

    @Test
    void equals_returnsTrue() {
        Workout workout1 = new Workout(111L, LocalDate.of(2026,7,9),12,70,8,WorkoutType.INTERVALS,CyclePhase.LUTEAL);
        Workout workout2 = new Workout(111L, LocalDate.of(2026,7,9),12,70,8,WorkoutType.INTERVALS,CyclePhase.LUTEAL);

        assertEquals(workout1, workout2);

    }

    @Test
    void equals_returnsFalse() {
        Workout workout1 = new Workout(111L, LocalDate.of(2026,7,6),12,75,8,WorkoutType.INTERVALS,CyclePhase.LUTEAL);
        Workout workout2 = new Workout(123L, LocalDate.of(2026,7,9),12,70,9,WorkoutType.INTERVALS,CyclePhase.LUTEAL);

        assertNotEquals(workout1, workout2);

    }
    @Test
    void hashCode_returnsSameValue_whenWorkoutsAreEqual() {
        Workout workout1 = new Workout(111L, LocalDate.of(2026,7,9),12,70,8,WorkoutType.INTERVALS,CyclePhase.LUTEAL);
        Workout workout2 = new Workout(111L, LocalDate.of(2026,7,9),12,70,8,WorkoutType.INTERVALS,CyclePhase.LUTEAL);

        assertEquals(workout1.hashCode(), workout2.hashCode());

    }

    @Test
    void hashSet_keepsOnlyOneWorkout() {

        Workout workout1 = new Workout(111L, LocalDate.of(2026,7,9),12,70,8,WorkoutType.INTERVALS,CyclePhase.LUTEAL);
        Workout workout2 = new Workout(111L, LocalDate.of(2026,7,9),12,70,8,WorkoutType.INTERVALS,CyclePhase.LUTEAL);

        Set<Workout> workouts = new HashSet<>();
        workouts.add(workout1);
        workouts.add(workout2);

        assertEquals(1,workouts.size());

    }

}