package com.alba.cycleruncoach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkoutServiceTest {

    private WorkoutService workoutService;
    private WorkoutRepository workoutRepository;
    @BeforeEach
    void setUp() {
        workoutRepository = new InMemoryWorkoutRepository();
        workoutService = new WorkoutService(workoutRepository);
    }

    @Test
    void calculateTotalDistanceByType_returnsCorrectValue() {
        User user = new User("Alba", "123");

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
        Workout workout = new Workout(777L,
                LocalDate.of(2026, 7, 3),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        workoutService.saveWorkout(workout);

        assertEquals(workout, workoutRepository.findAll().get(0));
    }

    @Test
    void saveWorkout_throwsException_whenWorkoutIsNull() {
        assertThrows(IllegalArgumentException.class, () -> workoutService.saveWorkout(null));
    }

    @Test
    void save_throwsException_whenIdAlreadyExists() {
        Workout workout = new Workout(777L,
                LocalDate.of(2026, 7, 3),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        Workout workout1 = new Workout(777L,
                LocalDate.of(2026, 7, 2),
                8.0,
                48,
                8,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        workoutService.saveWorkout(workout);
        assertThrows(IllegalArgumentException.class, () -> workoutService.saveWorkout(workout1));
    }

    @Test
    void findAllWorkouts_returnsSavedWorkouts() {
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
        assertEquals(2, workouts.size());

    }

    @Test
    void findAll_returnsDefensiveCopy() {
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
        workouts.removeAll(workoutService.findAllWorkouts());

        assertEquals(2, workoutService.findAllWorkouts().size());
    }

    @Test
    void findWorkoutById_returnsWorkout_whenWorkoutExists(){
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
    void findById_throwsException_whenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> workoutService.findWorkoutById(null));
    }

    @Test
    void deleteWorkoutById_deletesWorkout_whenWorkoutExists() {

        Workout workout = new Workout(777L,
                LocalDate.of(2026, 7, 3),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        workoutService.saveWorkout(workout);

        assertTrue(workoutService.deleteWorkoutById(777L));
        assertNull(workoutService.findWorkoutById(777L));

    }

    @Test
    void deleteWorkoutById_returnsFalse_whenWorkoutDoesNotExist() {

        assertFalse(workoutService.deleteWorkoutById(777L));

    }

    @Test
    void deleteById_throwsException_whenIdIsNull()  {
        assertThrows(IllegalArgumentException.class, () -> workoutService.deleteWorkoutById(null));
    }
}