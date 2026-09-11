package com.alba.cycleruncoach.controller.dto;

import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.EnergyLevel;
import com.alba.cycleruncoach.domain.SleepQuality;
import com.alba.cycleruncoach.domain.Symptom;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record UpdateDailyCheckInRequest(

        @NotNull(message = "Date is required")
        @PastOrPresent(message = "Date cannot be in the future")
        LocalDate date,

        @NotNull(message = "Cycle phase is required")
        CyclePhase cyclePhase,

        @NotNull(message = "Energy level is required")
        EnergyLevel energyLevel,

        @NotNull(message = "Sleep quality is required")
        SleepQuality sleepQuality,

        @NotNull(message = "Symptoms are required")
        Set<@NotNull(message = "Symptoms cannot contain null") Symptom> symptoms,

        @NotNull(message = "Sleep hours are required")
        @DecimalMin(
                value = "0.0",
                message = "Sleep hours must be at least 0"
        )
        @DecimalMax(
                value = "24.0",
                message = "Sleep hours must be at most 24"
        )
        Double sleepHours
) {
}
