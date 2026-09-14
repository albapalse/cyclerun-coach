package com.alba.cycleruncoach.repository;

import com.alba.cycleruncoach.domain.Workout;

import java.util.List;
import java.util.Optional;

public interface WorkoutRepository {

    void save(Workout workout);

    List<Workout> findAll();

    Optional<Workout> findById(Long id);

    boolean update(Workout workout);

    boolean deleteById(Long id);
}
