package com.alba.cycleruncoach;

import org.junit.jupiter.api.Test;

import java.util.*;
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
        Workout workout = new Workout(LocalDate.of(2026, 7, 1), 10.0, 50, 6, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);

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
        Workout workout = new Workout(LocalDate.of(2026, 7, 1), 10.0, 50, 6, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);

        user.addWorkout(workout);

        assertThrows(IllegalArgumentException.class, () -> {
            user.addWorkout(workout);
        });
    }

    @Test
    void removeWorkout_removesExistingWorkout() {
        User user = new User("Alba", "123");
        Workout workout = new Workout(LocalDate.of(2026, 7, 1), 10.0, 50, 6, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);

        user.addWorkout(workout);

        boolean removed = user.removeWorkout(workout);

        assertTrue(removed);
        assertFalse(user.getWorkouts().contains(workout));
        assertTrue(user.getWorkouts().isEmpty());
    }

    @Test
    void removeWorkout_returnsFalse_whenWorkoutDoesNotExist() {
        User user = new User("Alba", "123");
        Workout workout = new Workout(LocalDate.of(2026, 7, 1), 10.0, 50, 6 , WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);

        boolean removed = user.removeWorkout(workout);

        assertFalse(removed);
        assertTrue(user.getWorkouts().isEmpty());
    }

    @Test
    void calculateTotalKms_returnsCorrectKms() {
        User user = new User("Alba", "123");

        Workout workout1 = new Workout(LocalDate.of(2026, 7, 1), 10.0, 50, 6,  WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);
        Workout workout2 = new Workout(LocalDate.of(2026, 7, 2), 12.0, 60, 5, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);

        user.addWorkout(workout1);
        user.addWorkout(workout2);

        double kms = user.calculateTotalKms();

        assertEquals(22.0, kms, 0.001);
    }

    @Test
    void getWorkouts_returnsCopyOfInternalList() {
        User user = new User("Alba", "123");
        Workout workout = new Workout(LocalDate.of(2026, 7, 1), 10.0, 50, 6,  WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);

        user.addWorkout(workout);

        List<Workout> workoutsCopy = user.getWorkouts();
        workoutsCopy.clear();

        assertEquals(1, user.getWorkouts().size());
    }

    @Test
    void countWorkouts_returnsCorrectCount() {
        User user = new User("Alba", "123");

        Workout workout1 = new Workout(LocalDate.of(2026, 07, 01), 10.0, 50, 6, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);
        Workout workout2 = new Workout(LocalDate.of(2026, 07, 02), 10.0, 50, 6, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);
        user.addWorkout(workout1);
        user.addWorkout(workout2);

        assertEquals(2, user.countWorkouts());
    }

    @Test
    void getWorkoutsByType_returnsCorrectList() {
        User user = new User("Alba", "123");
        Workout workout1 = new Workout(LocalDate.of(2026, 07, 01), 10.0, 50, 6, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);
        Workout workout2 = new Workout(LocalDate.of(2026, 07, 02), 10.0, 50, 6, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);

        user.addWorkout(workout1);
        user.addWorkout(workout2);
        List<Workout> workoutsByType = user.getWorkoutsByType(WorkoutType.INTERVALS);

        assertArrayEquals(new Workout[]{workout1, workout2}, workoutsByType.toArray());
    }

    @Test
    void getWorkoutsByType_returnsOnlyWorkoutsOfGivenType() {
        User user = new User("Alba", "123");

        Workout easyRun = new Workout(
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                4,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        Workout longRun = new Workout(
                LocalDate.of(2026, 7, 2),
                15.0,
                90,
                6,
                WorkoutType.LONG_RUN,
                CyclePhase.FOLLICULAR
        );

        user.addWorkout(easyRun);
        user.addWorkout(longRun);

        List<Workout> longRuns = user.getWorkoutsByType(WorkoutType.LONG_RUN);

        assertEquals(1, longRuns.size());
        assertTrue(longRuns.contains(longRun));
        assertFalse(longRuns.contains(easyRun));
    }

    @Test
    void getWorkoutsByType_throwsException_whenTypeIsNull() {
        User user = new User("Alba", "123");
        assertThrows(IllegalArgumentException.class, () -> {
            user.getWorkoutsByType(null);
        });

    }
    @Test
    void getWorkoutsByCyclePhase_returnsOnlyWorkoutsOfGivenCyclePhase() {
        User user = new User("Alba", "123");

        Workout follicularWorkout = new Workout(
                LocalDate.of(2026, 7, 1),
                10.0,
                50,
                6,
                WorkoutType.INTERVALS,
                CyclePhase.FOLLICULAR
        );

        Workout ovulatoryWorkout = new Workout(
                LocalDate.of(2026, 7, 2),
                10.0,
                50,
                6,
                WorkoutType.INTERVALS,
                CyclePhase.OVULATORY
        );

        user.addWorkout(follicularWorkout);
        user.addWorkout(ovulatoryWorkout);

        List<Workout> follicularWorkouts = user.getWorkoutsByCyclePhase(CyclePhase.FOLLICULAR);

        assertEquals(1, follicularWorkouts.size());
        assertTrue(follicularWorkouts.contains(follicularWorkout));
        assertFalse(follicularWorkouts.contains(ovulatoryWorkout));
    }

    @Test
    void getWorkoutsByCyclePhase_throwsException_whenCyclePhaseIsNull() {
        User user = new User("Alba", "123");
        assertThrows(IllegalArgumentException.class, () -> {user.getWorkoutsByCyclePhase(null);});
    }


    @Test
    void countHighIntensityWorkouts_returnsCorrectCount() {
        User user = new User("Alba", "123");
        Workout workout1 = new Workout(LocalDate.of(2026, 07, 01), 10.0, 50, 6, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);
        Workout workout2 = new Workout(LocalDate.of(2026, 07, 02), 10.0, 50, 6, WorkoutType.INTERVALS, CyclePhase.OVULATORY);

        user.addWorkout(workout1);
        user.addWorkout(workout2);

        int countHighIntensityWorkouts = user.countHighIntensityWorkouts();

        assertEquals(2, countHighIntensityWorkouts);
    }

    @Test
    void calculateAveragePace_returnsCorrectAveragePace() {
        User user = new User("Alba", "123");
        Workout workout1 = new Workout(LocalDate.of(2026, 07, 01), 10.0, 50, 6, WorkoutType.INTERVALS, CyclePhase.FOLLICULAR);
        Workout workout2 = new Workout(LocalDate.of(2026, 07, 02), 10.0, 50, 6, WorkoutType.INTERVALS, CyclePhase.OVULATORY);

        user.addWorkout(workout1);
        user.addWorkout(workout2);
        double averagePace = user.calculateAveragePace();

        assertEquals(5.0, averagePace, 0.0001);
    }

    @Test
    void calculateAveragePace_returnsZeroWhenNoWorkouts() {
        User user = new User("Alba", "123");
        double averagePace = user.calculateAveragePace();
        assertEquals(0.0, averagePace, 0.0001);
    }

    @Test
    void getUsedWorkoutTypes_returnsCorrectTypes() {
        User user = new User("Alba", "123");
        Workout easyRun1 = new Workout(
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                4,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        Workout easyRun2 = new Workout(
                LocalDate.of(2026, 7, 2),
                6.0,
                36,
                4,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        Workout intervals = new Workout(
                LocalDate.of(2026, 7, 3),
                5.0,
                30,
                7,
                WorkoutType.INTERVALS,
                CyclePhase.OVULATORY
        );
        user.addWorkout(easyRun1);
        user.addWorkout(easyRun2);
        user.addWorkout(intervals);

        Set<WorkoutType> usedWorkoutTypes = user.getUsedWorkoutTypes();

        assertEquals(2, usedWorkoutTypes.size());
        assertTrue(usedWorkoutTypes.contains(WorkoutType.EASY_RUN));
        assertTrue(usedWorkoutTypes.contains(WorkoutType.INTERVALS));


    }

    @Test
    void countWorkoutsByType_returnsCorrectCount() {
        User user = new User("Alba", "123");
        Workout easyRun1 = new Workout(
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                4,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        Workout easyRun2 = new Workout(
                LocalDate.of(2026, 7, 2),
                6.0,
                36,
                4,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        Workout longRun = new Workout(
                LocalDate.of(2026, 7, 3),
                15.0,
                90,
                6,
                WorkoutType.LONG_RUN,
                CyclePhase.LUTEAL
        );
        user.addWorkout(easyRun1);
        user.addWorkout(easyRun2);
        user.addWorkout(longRun);

        Map<WorkoutType, Integer> countWorkoutsByType = user.countWorkoutsByType();

        assertEquals(2, countWorkoutsByType.get(WorkoutType.EASY_RUN));
        assertEquals(1, countWorkoutsByType.get(WorkoutType.LONG_RUN));

    }

    @Test
    void groupWorkoutsByCyclePhase_returnsCorrectGroup() {
        User user = new User("Alba", "123");

        Workout follicularWorkout1 = new Workout(
                LocalDate.of(2026, 7, 1),
                8.0,
                48,
                4,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        Workout follicularWorkout2 = new Workout(
                LocalDate.of(2026, 7, 2),
                5.0,
                30,
                7,
                WorkoutType.INTERVALS,
                CyclePhase.FOLLICULAR
        );

        Workout lutealWorkout = new Workout(
                LocalDate.of(2026, 7, 10),
                6.0,
                40,
                6,
                WorkoutType.RECOVERY_RUN,
                CyclePhase.LUTEAL
        );

        user.addWorkout(follicularWorkout1);
        user.addWorkout(follicularWorkout2);
        user.addWorkout(lutealWorkout);

        Map<CyclePhase, List<Workout>> groupWorkoutsByCyclePhase = user.groupWorkoutsByCyclePhase();

        assertEquals(2, groupWorkoutsByCyclePhase.get(CyclePhase.FOLLICULAR).size());
        assertEquals(1, groupWorkoutsByCyclePhase.get(CyclePhase.LUTEAL).size());

        assertTrue(groupWorkoutsByCyclePhase.get(CyclePhase.LUTEAL).contains(lutealWorkout));
        assertTrue(groupWorkoutsByCyclePhase.get(CyclePhase.FOLLICULAR).contains(follicularWorkout1));
        assertTrue(groupWorkoutsByCyclePhase.get(CyclePhase.FOLLICULAR).contains(follicularWorkout2));

    }

    @Test
    void addWorkout_throwsException_whenEquivalentWorkoutAlreadyExists() {
        User user = new User("Alba", "123");
        Workout workout1 = new Workout(LocalDate.of(2026,7,11),18,95,7,WorkoutType.LONG_RUN,CyclePhase.FOLLICULAR);
        Workout workout2 = new Workout(LocalDate.of(2026,7,11),18,95,7,WorkoutType.LONG_RUN,CyclePhase.FOLLICULAR);

        user.addWorkout(workout1);

        assertThrows(IllegalArgumentException.class, () -> user.addWorkout(workout2));
    }



    }