package com.alba.cycleruncoach.service;

import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.Workout;
import com.alba.cycleruncoach.domain.WorkoutType;
import com.alba.cycleruncoach.exception.DuplicateResourceException;
import com.alba.cycleruncoach.repository.WorkoutRepository;
import com.alba.cycleruncoach.repository.memory.InMemoryWorkoutRepository;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkoutServiceTest {

    private WorkoutService workoutService;
    private WorkoutRepository workoutRepository;

    @BeforeEach
    void setUp() {
        workoutRepository = new InMemoryWorkoutRepository();
        workoutService = new WorkoutService(workoutRepository);
    }

    @Test
    void constructor_throwsException_whenRepositoryIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new WorkoutService(null)
        );
    }

    @Test
    void saveWorkout_savesWorkoutInRepository() {
        Workout workout = createWorkout(777L);

        workoutService.saveWorkout(workout);

        assertEquals(workout, workoutRepository.findAll().get(0));
    }

    @Test
    void saveWorkout_throwsException_whenWorkoutIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> workoutService.saveWorkout(null)
        );
    }

    @Test
    void saveWorkout_throwsException_whenIdAlreadyExists() {
        Workout firstWorkout = createWorkout(777L);
        Workout duplicateWorkout = new Workout(
                777L,
                LocalDate.of(2026, 7, 2),
                8.0,
                48,
                8,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
        workoutService.saveWorkout(firstWorkout);

        assertThrows(
                DuplicateResourceException.class,
                () -> workoutService.saveWorkout(duplicateWorkout)
        );
    }

    @Test
    void findAllWorkouts_returnsSavedWorkouts() {
        Workout firstWorkout = createWorkout(777L);
        Workout secondWorkout = createWorkout(666L);
        workoutService.saveWorkout(firstWorkout);
        workoutService.saveWorkout(secondWorkout);

        List<Workout> workouts = workoutService.findAllWorkouts();

        assertEquals(List.of(firstWorkout, secondWorkout), workouts);
    }

    @Test
    void findAllWorkouts_returnsDefensiveCopy() {
        Workout firstWorkout = createWorkout(777L);
        Workout secondWorkout = createWorkout(666L);
        workoutService.saveWorkout(firstWorkout);
        workoutService.saveWorkout(secondWorkout);

        List<Workout> workouts = workoutService.findAllWorkouts();
        workouts.clear();

        assertEquals(2, workoutService.findAllWorkouts().size());
    }

    @Test
    void findWorkoutById_returnsWorkout_whenWorkoutExists() {
        Workout workout = createWorkout(777L);
        workoutService.saveWorkout(workout);

        Workout foundWorkout =
                workoutService.findWorkoutById(777L).orElseThrow();

        assertEquals(workout, foundWorkout);
    }

    @Test
    void findWorkoutById_returnsEmpty_whenWorkoutDoesNotExist() {
        assertTrue(workoutService.findWorkoutById(777L).isEmpty());
    }

    @Test
    void findWorkoutById_throwsException_whenIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> workoutService.findWorkoutById(null)
        );
    }

    @Test
    void updateWorkout_updatesWorkout_whenWorkoutExists() {
        Workout originalWorkout = createWorkout(777L);
        Workout updatedWorkout = new Workout(
                777L,
                LocalDate.of(2026, 7, 3),
                10.0,
                55,
                8,
                WorkoutType.TEMPO_RUN,
                CyclePhase.LUTEAL
        );
        workoutService.saveWorkout(originalWorkout);

        boolean updated = workoutService.updateWorkout(updatedWorkout);

        assertTrue(updated);
        assertEquals(
                updatedWorkout,
                workoutService.findWorkoutById(777L).orElseThrow()
        );
    }

    @Test
    void updateWorkout_returnsFalse_whenWorkoutDoesNotExist() {
        boolean updated = workoutService.updateWorkout(createWorkout(777L));

        assertFalse(updated);
        assertTrue(workoutService.findAllWorkouts().isEmpty());
    }

    @Test
    void deleteWorkoutById_deletesWorkout_whenWorkoutExists() {
        Workout workout = createWorkout(777L);
        workoutService.saveWorkout(workout);

        boolean deleted = workoutService.deleteWorkoutById(777L);

        assertTrue(deleted);
        assertTrue(workoutService.findWorkoutById(777L).isEmpty());
    }

    @Test
    void deleteWorkoutById_returnsFalse_whenWorkoutDoesNotExist() {
        assertFalse(workoutService.deleteWorkoutById(777L));
    }

    @Test
    void deleteWorkoutById_throwsException_whenIdIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> workoutService.deleteWorkoutById(null)
        );
    }

    private Workout createWorkout(Long id) {
        return new Workout(
                id,
                LocalDate.of(2026, 7, 3),
                8.0,
                48,
                7,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );
    }
}
