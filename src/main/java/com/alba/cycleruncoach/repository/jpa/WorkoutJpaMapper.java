package com.alba.cycleruncoach.repository.jpa;

import com.alba.cycleruncoach.domain.Workout;

final class WorkoutJpaMapper {

    private WorkoutJpaMapper() {
    }

    static WorkoutJpaEntity toEntity(Workout workout) {
        return new WorkoutJpaEntity(
                workout.getId(),
                workout.getDate(),
                workout.getDistanceKm(),
                workout.getDurationMinutes(),
                workout.getPerceivedEffort(),
                workout.getWorkoutType(),
                workout.getCyclePhase()
        );
    }

    static Workout toDomain(WorkoutJpaEntity entity) {
        return new Workout(
                entity.getId(),
                entity.getDate(),
                entity.getDistanceKm(),
                entity.getDurationMinutes(),
                entity.getPerceivedEffort(),
                entity.getWorkoutType(),
                entity.getCyclePhase()
        );
    }
}