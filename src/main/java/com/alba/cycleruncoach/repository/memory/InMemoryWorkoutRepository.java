package com.alba.cycleruncoach.repository.memory;

import com.alba.cycleruncoach.domain.Workout;
import com.alba.cycleruncoach.exception.DuplicateResourceException;
import com.alba.cycleruncoach.repository.WorkoutRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryWorkoutRepository implements WorkoutRepository {

    private final List<Workout> workouts;

    public InMemoryWorkoutRepository() {
        this.workouts = new ArrayList<>();
    }

    @Override
    public void save(Workout workout) {
        validateWorkout(workout);

        if (findById(workout.getId()).isPresent()) {
            throw new DuplicateResourceException(
                    "A workout with ID " + workout.getId() + " already exists. Please use a different ID."
            );
        }

        workouts.add(workout);
    }

    @Override
    public List<Workout> findAll() {
        return new ArrayList<>(workouts);
    }

    @Override
    public Optional<Workout> findById(Long id) {
        validateId(id);

        for (Workout workout : workouts) {
            if (workout.getId().equals(id)) {
                return Optional.of(workout);
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean update(Workout workout) {
        validateWorkout(workout);

        for (int index = 0; index < workouts.size(); index++) {
            Workout storedWorkout = workouts.get(index);

            if (storedWorkout.getId().equals(workout.getId())) {
                workouts.set(index, workout);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean deleteById(Long id) {
        validateId(id);

        Optional<Workout> workoutToDelete = findById(id);

        if (workoutToDelete.isEmpty()) {
            return false;
        }

        workouts.remove(workoutToDelete.get());
        return true;
    }

    private void validateWorkout(Workout workout) {
        if (workout == null) {
            throw new IllegalArgumentException("Workout cannot be null");
        }
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "Id must be greater than zero"
            );
        }
    }
}
