package com.alba.cycleruncoach;

import java.time.LocalDate;

public class Workout {

    private final LocalDate date;
    private final double distanceKm;
    private final int durationMinutes;
    private final int perceivedEffort;

    public Workout(LocalDate date, double distanceKm, int durationMinutes, int perceivedEffort) {
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

        this.date = date;
        this.distanceKm = distanceKm;
        this.durationMinutes = durationMinutes;
        this.perceivedEffort = perceivedEffort;
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

    public double calculatePace() {
        return durationMinutes / distanceKm;
    }

    public int calculateTrainingLoad() {
        return (int) Math.round(distanceKm * perceivedEffort);
    }
}