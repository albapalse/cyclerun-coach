package com.alba.cycleruncoach.controller.dto;

import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.WorkoutType;

import java.time.LocalDate;

public record WorkoutResponse(
        Long id,
        LocalDate date,
        double distanceKm,
        int durationMinutes,
        int perceivedEffort,
        WorkoutType workoutType,
        CyclePhase cyclePhase
) {
}
