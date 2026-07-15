package com.alba.cycleruncoach;

import java.util.ArrayList;
import java.util.List;

public class InMemoryWorkoutRepository implements WorkoutRepository {

    private final List<Workout> workouts;

    public InMemoryWorkoutRepository() {
        this.workouts = new ArrayList<>();
    }

    @Override
    public void save(Workout workout) {
        validateWorkout(workout);

        if (findById(workout.getId()) != null) {
            throw new IllegalArgumentException("Workout with this id already exists");
        }

        workouts.add(workout);
    }

    @Override
    public List<Workout> findAll() {
        return new ArrayList<>(workouts);
    }

    @Override
    public Workout findById(Long id) {
        validateId(id);

        for (Workout workout : workouts) {
            if (workout.getId().equals(id)) {
                return workout;
            }
        }

        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        validateId(id);

        Workout workoutToDelete = findById(id);

        if (workoutToDelete == null) {
            return false;
        }

        workouts.remove(workoutToDelete);
        return true;
    }

    private void validateWorkout(Workout workout) {
        if (workout == null) {
            throw new IllegalArgumentException("Workout cannot be null");
        }
    }

    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
    }
}