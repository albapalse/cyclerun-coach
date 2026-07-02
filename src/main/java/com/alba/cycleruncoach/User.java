package com.alba.cycleruncoach;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final List<Workout> workouts;
    private final String username;
    private final String password;

    public User(String username, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }

        this.workouts = new ArrayList<>();
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public List<Workout> getWorkouts() {
        return new ArrayList<>(workouts);
    }

    public void addWorkout(Workout workout) {
        if (workout == null) {
            throw new IllegalArgumentException("Workout cannot be null");
        }

        if (workouts.contains(workout)) {
            throw new IllegalArgumentException("Workout already exists");
        }

        workouts.add(workout);
    }

    public boolean removeWorkout(Workout workout) {
        if (workout == null) {
            throw new IllegalArgumentException("Workout cannot be null");
        }

        return workouts.remove(workout);
    }

    public double calculateTotalKms() {
        double totalKms = 0;

        for (Workout workout : workouts) {
            totalKms += workout.getDistanceKm();
        }

        return totalKms;
    }

}