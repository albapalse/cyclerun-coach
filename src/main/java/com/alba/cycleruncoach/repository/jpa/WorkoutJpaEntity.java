package com.alba.cycleruncoach.repository.jpa;

import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.WorkoutType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "workouts")
public class WorkoutJpaEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "distance_km", nullable = false)
    private double distanceKm;

    @Column(name = "duration_minutes", nullable = false)
    private int durationMinutes;

    @Column(name = "perceived_effort", nullable = false)
    private int perceivedEffort;

    @Enumerated(EnumType.STRING)
    @Column(name = "workout_type", nullable = false)
    private WorkoutType workoutType;

    @Enumerated(EnumType.STRING)
    @Column(name = "cycle_phase", nullable = false)
    private CyclePhase cyclePhase;

    protected WorkoutJpaEntity() {
    }

    WorkoutJpaEntity(
            Long id,
            LocalDate date,
            double distanceKm,
            int durationMinutes,
            int perceivedEffort,
            WorkoutType workoutType,
            CyclePhase cyclePhase
    ) {
        this.id = id;
        this.date = date;
        this.distanceKm = distanceKm;
        this.durationMinutes = durationMinutes;
        this.perceivedEffort = perceivedEffort;
        this.workoutType = workoutType;
        this.cyclePhase = cyclePhase;
    }

    Long getId() {
        return id;
    }

    LocalDate getDate() {
        return date;
    }

    double getDistanceKm() {
        return distanceKm;
    }

    int getDurationMinutes() {
        return durationMinutes;
    }

    int getPerceivedEffort() {
        return perceivedEffort;
    }

    WorkoutType getWorkoutType() {
        return workoutType;
    }

    CyclePhase getCyclePhase() {
        return cyclePhase;
    }
}
