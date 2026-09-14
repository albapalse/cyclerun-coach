package com.alba.cycleruncoach.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class User {

    private final String username;
    private final List<Workout> workouts;

    public User(String username) {
        validateUser(username);
        this.username = username;
        this.workouts = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public List<Workout> getWorkouts() {
        // Returning a copy prevents callers from changing the user's internal list.
        return new ArrayList<>(workouts);
    }

    public void addWorkout(Workout workout) {
        validateWorkout(workout);

        if (workouts.contains(workout)) {
            throw new IllegalArgumentException("Workout already exists");
        }

        workouts.add(workout);
    }

    public boolean removeWorkout(Workout workout) {
        validateWorkout(workout);

        return workouts.remove(workout);
    }

    public double calculateTotalKms() {
        return workouts.stream().mapToDouble(workout -> workout.getDistanceKm()).sum();
    }

    public int countWorkouts() {
        return workouts.size();
    }


    public List<Workout> getWorkoutsByType(WorkoutType workoutType) {
        if (workoutType == null) {
            throw new IllegalArgumentException("Workout type cannot be null");
        }
        return workouts.stream()
                .filter(workout -> workout.getWorkoutType() == workoutType)
                .toList();
    }

    public List<Workout> getWorkoutsByCyclePhase(CyclePhase cyclePhase) {
        if (cyclePhase == null) {
            throw new IllegalArgumentException("Cycle phase cannot be null");
        }
        List<Workout> workoutsByCyclePhase = new ArrayList<>();
        for (Workout workout : workouts) {
            if (workout.getCyclePhase() == cyclePhase) {
                workoutsByCyclePhase.add(workout);
            }

        }
        return workoutsByCyclePhase;
    }

    public List<Workout> getWorkoutsByMinimumDistance(double minimumDistanceKm) {
        if (minimumDistanceKm <= 0) {
            throw new IllegalArgumentException("Minimum distance must be greater than zero");
        }

        return workouts.stream()
                .filter(workout -> workout.getDistanceKm() >= minimumDistanceKm)
                .toList();
    }

    public List<Workout> getWorkoutsSortedByDate() {
        return workouts.stream()
                .sorted((first, second) -> first.getDate().compareTo(second.getDate()))
                .toList();
    }

    private void validateWorkout(Workout workout) {
        if (workout == null) {
            throw new IllegalArgumentException("Workout cannot be null");
        }
    }

    private void validateUser(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
    }

    public int countHighIntensityWorkouts() {
        return (int) workouts.stream()
                .filter(Workout::isHighIntensity)
                .count();
    }

    public double calculateAveragePace() {
        if (workouts.isEmpty()) {
            return 0.0;
        }
        double pace = 0;
        for (Workout workout : workouts) {
            pace += workout.calculatePace();
        }
        return pace / workouts.size();
    }


    public Set<WorkoutType> getUsedWorkoutTypes() {
        return workouts.stream()
                .map(Workout::getWorkoutType)
                .collect(Collectors.toSet());
    }

    public Map<WorkoutType, Integer> countWorkoutsByType() {
        Map<WorkoutType, Integer> countWorkoutsByType = new HashMap<>();
        for (Workout workout : workouts) {
            WorkoutType workoutType = workout.getWorkoutType();

            int currentCount = countWorkoutsByType.getOrDefault(workoutType, 0);
            countWorkoutsByType.put(workoutType, currentCount + 1);
        }
        return countWorkoutsByType;
    }

    public Map<CyclePhase, List<Workout>> groupWorkoutsByCyclePhase() {
        Map<CyclePhase, List<Workout>> workoutsByCyclePhase = new HashMap<>();
        for (Workout workout : workouts) {

            CyclePhase cyclePhase = workout.getCyclePhase();

            if (!workoutsByCyclePhase.containsKey(cyclePhase)) {
                workoutsByCyclePhase.put(cyclePhase, new ArrayList<>());
            }

            workoutsByCyclePhase.get(cyclePhase).add(workout);
        }
        return workoutsByCyclePhase;
    }


}
