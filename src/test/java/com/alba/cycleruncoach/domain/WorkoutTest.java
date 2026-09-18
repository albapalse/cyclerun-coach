package com.alba.cycleruncoach.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkoutTest {

    private static final Long VALID_ID = 111L;
    private static final LocalDate VALID_DATE = LocalDate.of(2026, 7, 1);
    private static final double VALID_DISTANCE = 10.0;
    private static final int VALID_DURATION = 60;
    private static final int VALID_EFFORT = 5;
    private static final WorkoutType VALID_TYPE = WorkoutType.EASY_RUN;
    private static final CyclePhase VALID_PHASE = CyclePhase.FOLLICULAR;

    @Test
    void constructor_createsWorkoutWithValidData() {
        Workout workout = createWorkout(
                VALID_ID,
                VALID_DATE,
                VALID_DISTANCE,
                VALID_DURATION,
                VALID_EFFORT,
                WorkoutType.INTERVALS,
                VALID_PHASE
        );

        assertEquals(VALID_ID, workout.getId());
        assertEquals(VALID_DATE, workout.getDate());
        assertEquals(VALID_DISTANCE, workout.getDistanceKm());
        assertEquals(VALID_DURATION, workout.getDurationMinutes());
        assertEquals(VALID_EFFORT, workout.getPerceivedEffort());
        assertEquals(WorkoutType.INTERVALS, workout.getWorkoutType());
        assertEquals(VALID_PHASE, workout.getCyclePhase());
    }

    @Test
    void calculatePace_returnsMinutesPerKilometer() {
        Workout workout = createValidWorkout();

        assertEquals(6.0, workout.calculatePace());
    }

    @Test
    void calculateTrainingLoad_returnsDistanceMultipliedByEffort() {
        Workout workout = createValidWorkout();

        assertEquals(50, workout.calculateTrainingLoad());
    }

    @Test
    void isHighIntensity_returnsTrue_whenEffortIsHigh() {
        Workout workout = createWorkout(
                VALID_ID,
                VALID_DATE,
                VALID_DISTANCE,
                VALID_DURATION,
                8,
                WorkoutType.LONG_RUN,
                CyclePhase.MENSTRUAL
        );

        assertTrue(workout.isHighIntensity());
    }

    @Test
    void isHighIntensity_returnsTrue_whenWorkoutTypeIsIntervals() {
        Workout workout = createWorkout(
                VALID_ID,
                VALID_DATE,
                VALID_DISTANCE,
                VALID_DURATION,
                5,
                WorkoutType.INTERVALS,
                VALID_PHASE
        );

        assertTrue(workout.isHighIntensity());
    }

    @Test
    void isHighIntensity_returnsFalse_forEasyRunWithModerateEffort() {
        assertFalse(createValidWorkout().isHighIntensity());
    }

    @Test
    void constructor_throwsException_whenIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createWorkout(
                        null, VALID_DATE, VALID_DISTANCE, VALID_DURATION,
                        VALID_EFFORT, VALID_TYPE, VALID_PHASE
                )
        );
    }

    @Test
    void constructor_throwsException_whenIdIsZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createWorkout(
                        0L, VALID_DATE, VALID_DISTANCE, VALID_DURATION,
                        VALID_EFFORT, VALID_TYPE, VALID_PHASE
                )
        );
    }

    @Test
    void constructor_throwsException_whenIdIsNegative() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createWorkout(
                        -1L, VALID_DATE, VALID_DISTANCE, VALID_DURATION,
                        VALID_EFFORT, VALID_TYPE, VALID_PHASE
                )
        );
    }

    @Test
    void constructor_throwsException_whenDateIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createWorkout(
                        VALID_ID, null, VALID_DISTANCE, VALID_DURATION,
                        VALID_EFFORT, VALID_TYPE, VALID_PHASE
                )
        );
    }

    @Test
    void constructor_throwsException_whenDistanceIsZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createWorkout(
                        VALID_ID, VALID_DATE, 0.0, VALID_DURATION,
                        VALID_EFFORT, VALID_TYPE, VALID_PHASE
                )
        );
    }

    @Test
    void constructor_throwsException_whenDistanceIsNegative() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createWorkout(
                        VALID_ID, VALID_DATE, -1.0, VALID_DURATION,
                        VALID_EFFORT, VALID_TYPE, VALID_PHASE
                )
        );
    }

    @Test
    void constructor_throwsException_whenDistanceIsNotFinite() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createWorkout(
                        VALID_ID, VALID_DATE, Double.NaN, VALID_DURATION,
                        VALID_EFFORT, VALID_TYPE, VALID_PHASE
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> createWorkout(
                        VALID_ID, VALID_DATE, Double.POSITIVE_INFINITY,
                        VALID_DURATION, VALID_EFFORT, VALID_TYPE, VALID_PHASE
                )
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> createWorkout(
                        VALID_ID, VALID_DATE, Double.NEGATIVE_INFINITY,
                        VALID_DURATION, VALID_EFFORT, VALID_TYPE, VALID_PHASE
                )
        );
    }

    @Test
    void constructor_throwsException_whenDurationIsZero() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createWorkout(
                        VALID_ID, VALID_DATE, VALID_DISTANCE, 0,
                        VALID_EFFORT, VALID_TYPE, VALID_PHASE
                )
        );
    }

    @Test
    void constructor_throwsException_whenDurationIsNegative() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createWorkout(
                        VALID_ID, VALID_DATE, VALID_DISTANCE, -1,
                        VALID_EFFORT, VALID_TYPE, VALID_PHASE
                )
        );
    }

    @Test
    void constructor_throwsException_whenPerceivedEffortIsLowerThanOne() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createWorkout(
                        VALID_ID, VALID_DATE, VALID_DISTANCE, VALID_DURATION,
                        0, VALID_TYPE, VALID_PHASE
                )
        );
    }

    @Test
    void constructor_throwsException_whenPerceivedEffortIsGreaterThanTen() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createWorkout(
                        VALID_ID, VALID_DATE, VALID_DISTANCE, VALID_DURATION,
                        11, VALID_TYPE, VALID_PHASE
                )
        );
    }

    @Test
    void constructor_throwsException_whenWorkoutTypeIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createWorkout(
                        VALID_ID, VALID_DATE, VALID_DISTANCE, VALID_DURATION,
                        VALID_EFFORT, null, VALID_PHASE
                )
        );
    }

    @Test
    void constructor_throwsException_whenCyclePhaseIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> createWorkout(
                        VALID_ID, VALID_DATE, VALID_DISTANCE, VALID_DURATION,
                        VALID_EFFORT, VALID_TYPE, null
                )
        );
    }

    @Test
    void equals_returnsTrue_whenIdsMatch() {
        Workout firstWorkout = createValidWorkout();
        Workout secondWorkout = createWorkout(
                VALID_ID,
                LocalDate.of(2026, 7, 10),
                5.0,
                35,
                3,
                WorkoutType.RECOVERY_RUN,
                CyclePhase.LUTEAL
        );

        assertEquals(firstWorkout, secondWorkout);
    }

    @Test
    void equals_returnsFalse_whenIdsDiffer() {
        Workout firstWorkout = createValidWorkout();
        Workout secondWorkout = createWorkout(
                123L,
                VALID_DATE,
                VALID_DISTANCE,
                VALID_DURATION,
                VALID_EFFORT,
                VALID_TYPE,
                VALID_PHASE
        );

        assertNotEquals(firstWorkout, secondWorkout);
    }

    @Test
    void hashCode_returnsSameValue_whenIdsMatch() {
        Workout firstWorkout = createValidWorkout();
        Workout secondWorkout = createWorkout(
                VALID_ID,
                LocalDate.of(2026, 7, 10),
                5.0,
                35,
                3,
                WorkoutType.RECOVERY_RUN,
                CyclePhase.LUTEAL
        );

        assertEquals(firstWorkout.hashCode(), secondWorkout.hashCode());
    }

    @Test
    void hashSet_keepsOnlyOneWorkout_whenIdsMatch() {
        Workout firstWorkout = createValidWorkout();
        Workout secondWorkout = createWorkout(
                VALID_ID,
                LocalDate.of(2026, 7, 10),
                5.0,
                35,
                3,
                WorkoutType.RECOVERY_RUN,
                CyclePhase.LUTEAL
        );

        Set<Workout> workouts = new HashSet<>();
        workouts.add(firstWorkout);
        workouts.add(secondWorkout);

        assertEquals(1, workouts.size());
    }

    private Workout createValidWorkout() {
        return createWorkout(
                VALID_ID,
                VALID_DATE,
                VALID_DISTANCE,
                VALID_DURATION,
                VALID_EFFORT,
                VALID_TYPE,
                VALID_PHASE
        );
    }

    private Workout createWorkout(
            Long id,
            LocalDate date,
            double distanceKm,
            int durationMinutes,
            int perceivedEffort,
            WorkoutType workoutType,
            CyclePhase cyclePhase
    ) {
        return new Workout(
                id,
                date,
                distanceKm,
                durationMinutes,
                perceivedEffort,
                workoutType,
                cyclePhase
        );
    }
}
