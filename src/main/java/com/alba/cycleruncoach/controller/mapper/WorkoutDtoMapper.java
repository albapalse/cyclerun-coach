package com.alba.cycleruncoach.controller.mapper;

import com.alba.cycleruncoach.controller.dto.CreateWorkoutRequest;
import com.alba.cycleruncoach.controller.dto.UpdateWorkoutRequest;
import com.alba.cycleruncoach.controller.dto.WorkoutResponse;
import com.alba.cycleruncoach.domain.Workout;

import org.springframework.stereotype.Component;

@Component
public class WorkoutDtoMapper {

    public Workout toDomain(CreateWorkoutRequest request) {
        return new Workout(
                request.id(),
                request.date(),
                request.distanceKm(),
                request.durationMinutes(),
                request.perceivedEffort(),
                request.workoutType(),
                request.cyclePhase()
        );
    }

    public Workout toDomain(
            Long id,
            UpdateWorkoutRequest request
    ) {
        return new Workout(
                id,
                request.date(),
                request.distanceKm(),
                request.durationMinutes(),
                request.perceivedEffort(),
                request.workoutType(),
                request.cyclePhase()
        );
    }

    public WorkoutResponse toResponse(Workout workout) {
        return new WorkoutResponse(
                workout.getId(),
                workout.getDate(),
                workout.getDistanceKm(),
                workout.getDurationMinutes(),
                workout.getPerceivedEffort(),
                workout.getWorkoutType(),
                workout.getCyclePhase()
        );
    }
}