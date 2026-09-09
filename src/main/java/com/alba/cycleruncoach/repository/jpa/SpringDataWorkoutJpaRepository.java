package com.alba.cycleruncoach.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataWorkoutJpaRepository
        extends JpaRepository<WorkoutJpaEntity, Long> {
}
