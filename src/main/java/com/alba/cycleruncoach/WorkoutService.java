package com.alba.cycleruncoach;

import java.util.List;

public class WorkoutService {

    public void registerWorkout(User user, Workout workout) {
        validateUser(user);

        user.addWorkout(workout);
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
}
