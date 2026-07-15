package com.alba.cycleruncoach;

import java.util.List;

public interface WorkoutRepository {

    void save(Workout workout);

    List<Workout> findAll();

    Workout findById(Long id);

    boolean deleteById(Long id);
}