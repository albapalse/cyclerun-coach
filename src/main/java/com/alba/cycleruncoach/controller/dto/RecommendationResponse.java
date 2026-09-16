package com.alba.cycleruncoach.controller.dto;

import com.alba.cycleruncoach.domain.WorkoutType;

import java.time.LocalDate;

public record RecommendationResponse(
        Long checkInId,
        LocalDate checkInDate,
        WorkoutType workoutType,
        int maxPerceivedEffort,
        String reason
) {
}
