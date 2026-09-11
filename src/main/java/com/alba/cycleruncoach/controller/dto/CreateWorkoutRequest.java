package com.alba.cycleruncoach.controller.dto;

import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.WorkoutType;

import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public record CreateWorkoutRequest(

        @NotNull(message = "Id is required")
        @Positive(message = "Id must be greater than zero")
        Long id,

        @NotNull(message = "Date is required")
        @PastOrPresent(message = "Date cannot be in the future")
        LocalDate date,

        @NotNull(message = "Distance is required")
        @DecimalMin(
                value = "0.0",
                inclusive = false,
                message = "Distance must be greater than zero"
        )
        Double distanceKm,

        @NotNull(message = "Duration is required")
        @Positive(message = "Duration must be greater than zero")
        Integer durationMinutes,

        @NotNull(message = "Perceived effort is required")
        @Min(value = 1, message = "Perceived effort must be at least 1")
        @Max(value = 10, message = "Perceived effort must be at most 10")
        Integer perceivedEffort,

        @NotNull(message = "Workout type is required")
        WorkoutType workoutType,

        @NotNull(message = "Cycle phase is required")
        CyclePhase cyclePhase
) {
}
