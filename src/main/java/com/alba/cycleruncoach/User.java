package com.alba.cycleruncoach;
import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class User {

    private final List<Workout> workouts;
    private final String username;
    private final String password;

    public User(String username, String password) {
        validateUser(username);
        validatePassword(password);
        this.workouts = new ArrayList<>();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }


    public List<Workout> getWorkouts() {
        return new ArrayList<>(workouts); //returns a COPY of the list
    }


    public void addWorkout(Workout workout) {
        validateWorkout(workout);

        if (workout == null) {
            throw new IllegalArgumentException("Workout cannot be null");
        }

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
        double totalKms = 0;

        for (Workout workout : workouts) {
            totalKms += workout.getDistanceKm();
        }

        return totalKms;
    }

    public int countWorkouts() {
        return workouts.size();
    }


    public List<Workout> getWorkoutsByType(WorkoutType workoutType) {
        if (workoutType == null) {
            throw new IllegalArgumentException("Workout type cannot be null");
        }
        List<Workout> totalWorkouts = new ArrayList<>();
        for (Workout workout : workouts) {
            if (workout.getWorkoutType() == workoutType) {
                totalWorkouts.add(workout);
            }
        }
        return totalWorkouts;
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

    private void validateWorkout(Workout workout) {
        if (workout == null) {
            throw new IllegalArgumentException("Workout cannot be null");
        }
    }

    public void validateUser(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
    }

    public void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
    }

    public int countHighIntensityWorkouts() {
        int totalHighIntensityWorkouts = 0;
        for (Workout workout : workouts) {
            if (workout.isHighIntensity()) {
                totalHighIntensityWorkouts++;
            }
        }
        return totalHighIntensityWorkouts;
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
        Set<WorkoutType> usedWorkoutTypes = new HashSet<>();
        for (Workout workout : workouts) {
            usedWorkoutTypes.add(workout.getWorkoutType());
        }
        return usedWorkoutTypes;
    }

    public Map<WorkoutType, Integer> countWorkoutsByType() {
        Map<WorkoutType, Integer> countWorkoutsByType = new HashMap<>();
        for (Workout workout : workouts) {
            WorkoutType workoutType = workout.getWorkoutType();

            if (countWorkoutsByType.containsKey(workoutType)) {
                int count = countWorkoutsByType.get(workoutType);
                countWorkoutsByType.put(workoutType, count + 1);

            } else {
                countWorkoutsByType.put(workoutType, 1);
            }
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