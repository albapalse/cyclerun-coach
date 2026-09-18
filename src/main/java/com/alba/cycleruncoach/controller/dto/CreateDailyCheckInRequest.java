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
import jakarta.validation.constraints.Positive;

public record CreateDailyCheckInRequest(

        @NotNull(message = "Please provide an ID.")
        @Positive(message = "Please use an ID greater than zero.")
        Long id,

        @NotNull(message = "Please provide a date.")
        @PastOrPresent(message = "Please use today or an earlier date.")
        LocalDate date,

        @NotNull(message = "Please select a cycle phase.")
        CyclePhase cyclePhase,

        @NotNull(message = "Please select an energy level.")
        EnergyLevel energyLevel,

        @NotNull(message = "Please select a sleep quality.")
        SleepQuality sleepQuality,

        @NotNull(message = "Please include the symptoms field. Use an empty list if there are none.")
        Set<@NotNull(message = "Please remove empty symptom values.") Symptom> symptoms,

        @NotNull(message = "Please provide the number of hours slept.")
        @DecimalMin(
                value = "0.0",
                message = "Sleep hours must be between 0 and 24."
        )
        @DecimalMax(
                value = "24.0",
                message = "Sleep hours must be between 0 and 24."
        )
        Double sleepHours
) {
}
