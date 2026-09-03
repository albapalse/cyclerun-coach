package com.alba.cycleruncoach.repository;

import com.alba.cycleruncoach.domain.Workout;

import java.util.List;

public interface WorkoutRepository {

    void save(Workout workout);

    List<Workout> findAll();

    Workout findById(Long id);

    boolean deleteById(Long id);
}