package com.alba.cycleruncoach;

import java.util.List;

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

    public Workout findWorkoutById(Long id) {
        return workoutRepository.findById(id);
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
