package com.alba.cycleruncoach.repository.jpa;

import com.alba.cycleruncoach.domain.Workout;
import com.alba.cycleruncoach.repository.WorkoutRepository;
import org.springframework.stereotype.Repository;
import com.alba.cycleruncoach.exception.DuplicateResourceException;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaWorkoutRepositoryAdapter implements WorkoutRepository {

    private final SpringDataWorkoutJpaRepository repository;

    public JpaWorkoutRepositoryAdapter(
            SpringDataWorkoutJpaRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public void save(Workout workout) {
        validateWorkout(workout);

        if (repository.existsById(workout.getId())) {
            throw new DuplicateResourceException(
                    "Workout with id " + workout.getId() + " already exists"
            );
        }

        repository.save(WorkoutJpaMapper.toEntity(workout));
    }

    @Override
    public List<Workout> findAll() {
        return repository.findAll()
                .stream()
                .map(WorkoutJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Workout> findById(Long id) {
        validateId(id);

        return repository.findById(id)
                .map(WorkoutJpaMapper::toDomain);
    }

    @Override
    public boolean update(Workout workout) {
        validateWorkout(workout);

        if (!repository.existsById(workout.getId())) {
            return false;
        }

        repository.save(WorkoutJpaMapper.toEntity(workout));
        return true;
    }

    @Override
    public boolean deleteById(Long id) {
        validateId(id);

        if (!repository.existsById(id)) {
            return false;
        }

        repository.deleteById(id);
        return true;
    }

    private void validateWorkout(Workout workout) {
        if (workout == null) {
            throw new IllegalArgumentException(
                    "Workout cannot be null"
            );
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
