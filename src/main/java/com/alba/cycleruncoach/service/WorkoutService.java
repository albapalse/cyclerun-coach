package com.alba.cycleruncoach.service;
import com.alba.cycleruncoach.domain.Workout;
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

    private void validateWorkoutRepository(WorkoutRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
    }
}
