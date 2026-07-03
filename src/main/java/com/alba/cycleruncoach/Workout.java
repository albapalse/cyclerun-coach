package com.alba.cycleruncoach;

import java.time.LocalDate;

public class Workout {

    private final LocalDate date;
    private final double distanceKm;
    private final int durationMinutes;
    private final int perceivedEffort;
    private final WorkoutType workoutType;
    private final CyclePhase cyclePhase;

    public Workout(LocalDate date, double distanceKm, int durationMinutes, int perceivedEffort, WorkoutType workoutType, CyclePhase cyclePhase) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if (distanceKm <= 0) {
            throw new IllegalArgumentException("Distance must be greater than zero");
        }

        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero");
        }

        if (perceivedEffort < 1 || perceivedEffort > 10) {
            throw new IllegalArgumentException("Perceived effort must be between 1 and 10");
        }

        if (workoutType == null) {
            throw new IllegalArgumentException("WorkoutType cannot be null");
        }
        if (cyclePhase == null) {
            throw new IllegalArgumentException("CyclePhase cannot be null");
        }

        this.date = date;
        this.distanceKm = distanceKm;
        this.durationMinutes = durationMinutes;
        this.perceivedEffort = perceivedEffort;
        this.workoutType = workoutType;
        this.cyclePhase = cyclePhase;
    }

    public LocalDate getDate() {
        return date;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getPerceivedEffort() {
        return perceivedEffort;
    }

    public WorkoutType getWorkoutType() {
        return workoutType;
    }

    public CyclePhase getCyclePhase() {
        return cyclePhase;
    }




    public double calculatePace() {
        return durationMinutes / distanceKm;
    }

    public int calculateTrainingLoad() {
        return (int) Math.round(distanceKm * perceivedEffort);
    }

    public boolean isHighIntensity() {
        return workoutType == WorkoutType.INTERVALS ||  workoutType ==WorkoutType.TEMPO_RUN || perceivedEffort >= 8;
    }

}