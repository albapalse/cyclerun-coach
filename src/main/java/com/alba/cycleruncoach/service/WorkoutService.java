package com.alba.cycleruncoach.service;

import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.User;
import com.alba.cycleruncoach.domain.Workout;
import com.alba.cycleruncoach.domain.WorkoutType;
import com.alba.cycleruncoach.repository.WorkoutRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    public WorkoutService(WorkoutRepository workoutRepository) {
        validateWorkoutRepository(workoutRepository);
        this.workoutRepository = workoutRepository;
    }

    public double calculateTotalDistanceByType(User user, WorkoutType workoutType) {
        validateUser(user);
        validateWorkoutType(workoutType);

        List<Workout> workouts = user.getWorkoutsByType(workoutType);

        double totalDistance = 0.0;

        for (Workout workout : workouts) {
            totalDistance += workout.getDistanceKm();
        }

        return totalDistance;
    }

    public double calculateAverageDistanceByCyclePhase(User user, CyclePhase cyclePhase) {
        validateUser(user);
        validateCyclePhase(cyclePhase);

        List<Workout> workouts = user.getWorkoutsByCyclePhase(cyclePhase);

        if (workouts.isEmpty()) {
            return 0.0;
        }

        double totalDistance = 0.0;

        for (Workout workout : workouts) {
            totalDistance += workout.getDistanceKm();
        }

        return totalDistance / workouts.size();
    }

    public void saveWorkout(Workout workout) {
        workoutRepository.save(workout);
    }

    public List<Workout> findAllWorkouts() {
        return workoutRepository.findAll();
    }

    public Optional<Workout> findWorkoutById(Long id) {
        return workoutRepository.findById(id);
    }
    public boolean updateWorkout(Workout workout) {
        return workoutRepository.update(workout);
    }

    public boolean deleteWorkoutById(Long id) {
        return workoutRepository.deleteById(id);
    }

    private void validateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
    }

    private void validateWorkoutType(WorkoutType workoutType) {
        if (workoutType == null) {
            throw new IllegalArgumentException("Workout type cannot be null");
        }
    }

    private void validateCyclePhase(CyclePhase cyclePhase) {
        if (cyclePhase == null) {
            throw new IllegalArgumentException("Cycle phase cannot be null");
        }
    }

    private void validateWorkoutRepository(WorkoutRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
    }
}
