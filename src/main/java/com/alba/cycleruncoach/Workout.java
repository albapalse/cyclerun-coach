package com.alba.cycleruncoach;

import java.time.LocalDate;
import java.util.Objects;

public class Workout {

    private final Long id;
    private final LocalDate date;
    private final double distanceKm;
    private final int durationMinutes;
    private final int perceivedEffort;
    private final WorkoutType workoutType;
    private final CyclePhase cyclePhase;

    public Workout(
            Long id,
            LocalDate date,
            double distanceKm,
            int durationMinutes,
            int perceivedEffort,
            WorkoutType workoutType,
            CyclePhase cyclePhase
    ) {
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

    public Long getId() {
        return id;
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

    private void validateDistanceKm(double distanceKm) {
        if (distanceKm <= 0) {
            throw new IllegalArgumentException("Distance must be greater than zero");
        }
    }

    private void validateDurationMinutes(int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Duration must be greater than zero");
        }
    }

    private void validatePerceivedEffort(int perceivedEffort) {
        if (perceivedEffort < 1 || perceivedEffort > 10) {
            throw new IllegalArgumentException("Perceived effort must be between 1 and 10");
        }
    }

    private void validateWorkoutType(WorkoutType workoutType) {
        if (workoutType == null) {
            throw new IllegalArgumentException("Workout type cannot be null");
        }
    }

    private void validateCyclePhase(CyclePhase cyclePhase) {
        if (cyclePhase == null) {
            throw new IllegalArgumentException("Cycle phase cannot be null");
        }
    }


    public double calculatePace() {
        return durationMinutes / distanceKm;
    }

    public int calculateTrainingLoad() {
        return (int) Math.round(distanceKm * perceivedEffort);
    }

    public boolean isHighIntensity() {
        return workoutType == WorkoutType.INTERVALS
                || workoutType == WorkoutType.TEMPO_RUN
                || perceivedEffort >= 8;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Workout workout = (Workout) o;

        // The id is intentionally excluded: workouts are equal when their training data matches.
        return Double.compare(workout.distanceKm, distanceKm) == 0
                && durationMinutes == workout.durationMinutes
                && perceivedEffort == workout.perceivedEffort
                && Objects.equals(date, workout.date)
                && workoutType == workout.workoutType
                && cyclePhase == workout.cyclePhase;
    }

    @Override
    public int hashCode() {
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
