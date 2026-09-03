package com.alba.cycleruncoach.domain;

import java.time.LocalDate;
import java.util.Set;

public class DailyCheckIn {
    private final Long id;
    private final LocalDate date;
    private final CyclePhase cyclePhase;
    private final EnergyLevel energyLevel;
    private final SleepQuality sleepQuality;
    private final Set<Symptom> symptoms;
    private final double sleepHours;

    public DailyCheckIn(
            Long id,
            LocalDate date,
            CyclePhase cyclePhase,
            EnergyLevel energyLevel,
            SleepQuality sleepQuality,
            Set<Symptom> symptoms,
            double sleepHours
    ) {
        validateId(id);
        validateDate(date);
        validateCyclePhase(cyclePhase);
        validateEnergyLevel(energyLevel);
        validateSleepQuality(sleepQuality);
        validateSymptoms(symptoms);
        validateSleepHours(sleepHours);

        this.id = id;
        this.date = date;
        this.cyclePhase = cyclePhase;
        this.energyLevel = energyLevel;
        this.sleepQuality = sleepQuality;
        this.symptoms = Set.copyOf(symptoms);
        this.sleepHours = sleepHours;
    }

    public boolean hasSymptom(Symptom symptom) {
        if (symptom == null) {
            throw new IllegalArgumentException("Symptom cannot be null");
        }

        return symptoms.contains(symptom);
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public CyclePhase getCyclePhase() {
        return cyclePhase;
    }

    public EnergyLevel getEnergyLevel() {
        return energyLevel;
    }

    public SleepQuality getSleepQuality() {
        return sleepQuality;
    }

    public Set<Symptom> getSymptoms() {
        return Set.copyOf(symptoms);
    }

    public double getSleepHours() {
        return sleepHours;
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id must be greater than zero");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
    }

    private void validateCyclePhase(CyclePhase cyclePhase) {
        if (cyclePhase == null) {
            throw new IllegalArgumentException("Cycle phase cannot be null");
        }
    }

    private void validateEnergyLevel(EnergyLevel energyLevel) {
        if (energyLevel == null) {
            throw new IllegalArgumentException("Energy level cannot be null");
        }
    }

    private void validateSleepQuality(SleepQuality sleepQuality) {
        if (sleepQuality == null) {
            throw new IllegalArgumentException("Sleep quality cannot be null");
        }
    }

    private void validateSymptoms(Set<Symptom> symptoms) {
        if (symptoms == null) {
            throw new IllegalArgumentException("Symptoms cannot be null");
        }

        for (Symptom symptom : symptoms) {
            if (symptom == null) {
                throw new IllegalArgumentException("Symptoms cannot contain null");
            }
        }
    }

    private void validateSleepHours(double sleepHours) {
        if (sleepHours < 0 || sleepHours > 24) {
            throw new IllegalArgumentException("Sleep hours must be between 0 and 24");
        }
    }

}
