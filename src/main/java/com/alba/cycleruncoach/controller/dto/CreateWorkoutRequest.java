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

        @NotNull(message = "Please provide an ID.")
        @Positive(message = "Please use an ID greater than zero.")
        Long id,

        @NotNull(message = "Please provide a date.")
        @PastOrPresent(message = "Please use today or an earlier date.")
        LocalDate date,

        @NotNull(message = "Please provide the distance in kilometers.")
        @DecimalMin(
                value = "0.0",
                inclusive = false,
                message = "Please enter a distance greater than zero."
        )
        Double distanceKm,

        @NotNull(message = "Please provide the duration in minutes.")
        @Positive(message = "Please enter a duration greater than zero.")
        Integer durationMinutes,

        @NotNull(message = "Please provide your perceived effort.")
        @Min(value = 1, message = "Perceived effort must be between 1 and 10.")
        @Max(value = 10, message = "Perceived effort must be between 1 and 10.")
        Integer perceivedEffort,

        @NotNull(message = "Please select a workout type.")
        WorkoutType workoutType,

        @NotNull(message = "Please select a cycle phase.")
        CyclePhase cyclePhase
) {
}
