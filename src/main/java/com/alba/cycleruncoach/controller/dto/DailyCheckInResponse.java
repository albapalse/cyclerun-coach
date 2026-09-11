package com.alba.cycleruncoach.controller.dto;

import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.EnergyLevel;
import com.alba.cycleruncoach.domain.SleepQuality;
import com.alba.cycleruncoach.domain.Symptom;

import java.time.LocalDate;
import java.util.Set;

public record DailyCheckInResponse(
        Long id,
        LocalDate date,
        CyclePhase cyclePhase,
        EnergyLevel energyLevel,
        SleepQuality sleepQuality,
        Set<Symptom> symptoms,
        double sleepHours
) {
}
