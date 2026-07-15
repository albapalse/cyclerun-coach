package com.alba.cycleruncoach;

import java.time.LocalDate;
import java.util.Objects;

public class Workout {

    private final LocalDate date;
    private final double distanceKm;
    private final int durationMinutes;
    private final int perceivedEffort;
    private final WorkoutType workoutType;
    private final CyclePhase cyclePhase;
    private final Long id;

    public Workout(Long id, LocalDate date, double distanceKm, int durationMinutes, int perceivedEffort, WorkoutType workoutType, CyclePhase cyclePhase) {
        validateId(id);
        validateDate(date);
        validateDistanceKm(distanceKm);
        validateDurationMinutes(durationMinutes);
        validatePerceivedEffort(perceivedEffort);
        validateWorkoutType(workoutType);
        validateCyclePhase(cyclePhase);

        this.id = id;
        this.date = date;
        this.distanceKm = distanceKm;
        this.durationMinutes = durationMinutes;
        this.perceivedEffort = perceivedEffort;
        this.workoutType = workoutType;
        this.cyclePhase = cyclePhase;
    }

    public Long getId() { return id; }

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

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Id must be greater than zero");
        }
    }

    public void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
    }

    public void validateDistanceKm(double distanceKm) {
        if (distanceKm <= 0) {
            throw new IllegalArgumentException("Distance must be greater than zero");
        }
    }

    public void validateDurationMinutes(int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero");
        }
    }

    public void validatePerceivedEffort(int perceivedEffort) {
        if (perceivedEffort < 1 || perceivedEffort > 10) {
            throw new IllegalArgumentException("Perceived effort must be between 1 and 10");
        }
    }

    public void validateWorkoutType(WorkoutType workoutType) {
        if (workoutType == null) {
            throw new IllegalArgumentException("WorkoutType cannot be null");
        }
    }

    public void validateCyclePhase(CyclePhase cyclePhase) {
        if (cyclePhase == null) {
            throw new IllegalArgumentException("CyclePhase cannot be null");
        }
    }


    public double calculatePace() {
        return durationMinutes / distanceKm;
    }

    public int calculateTrainingLoad() {
        return (int) Math.round(distanceKm * perceivedEffort);
    }

    public boolean isHighIntensity() {
        return workoutType == WorkoutType.INTERVALS || workoutType == WorkoutType.TEMPO_RUN || perceivedEffort >= 8;
    }

    @Override
    public boolean equals(Object o) {
        // Same object in memory.
        if (this == o) {
            return true;
        }

        // Null or different class.
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        // Safe cast after checking the class.
        Workout workout = (Workout) o;

        // Logical equality: same relevant attributes.
        return Double.compare(workout.distanceKm, distanceKm) == 0
                && durationMinutes == workout.durationMinutes
                && perceivedEffort == workout.perceivedEffort
                && Objects.equals(date, workout.date)
                && workoutType == workout.workoutType
                && cyclePhase == workout.cyclePhase;
    }

    @Override
    public int hashCode() {
        // Must use the same attributes as equals().
        return Objects.hash(
                date,
                distanceKm,
                durationMinutes,
                perceivedEffort,
                workoutType,
                cyclePhase
        );
    }
}