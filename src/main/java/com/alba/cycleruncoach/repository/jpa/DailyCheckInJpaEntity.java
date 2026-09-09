package com.alba.cycleruncoach.repository.jpa;

import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.EnergyLevel;
import com.alba.cycleruncoach.domain.SleepQuality;
import com.alba.cycleruncoach.domain.Symptom;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "daily_check_ins")
public class DailyCheckInJpaEntity {

    @Id
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "cycle_phase", nullable = false)
    private CyclePhase cyclePhase;

    @Enumerated(EnumType.STRING)
    @Column(name = "energy_level", nullable = false)
    private EnergyLevel energyLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "sleep_quality", nullable = false)
    private SleepQuality sleepQuality;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "daily_check_in_symptoms",
            joinColumns = @JoinColumn(name = "daily_check_in_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "symptom", nullable = false)
    private Set<Symptom> symptoms = new HashSet<>();

    @Column(name = "sleep_hours", nullable = false)
    private double sleepHours;

    protected DailyCheckInJpaEntity() {
    }

    DailyCheckInJpaEntity(
            Long id,
            LocalDate date,
            CyclePhase cyclePhase,
            EnergyLevel energyLevel,
            SleepQuality sleepQuality,
            Set<Symptom> symptoms,
            double sleepHours
    ) {
        this.id = id;
        this.date = date;
        this.cyclePhase = cyclePhase;
        this.energyLevel = energyLevel;
        this.sleepQuality = sleepQuality;
        this.symptoms = new HashSet<>(symptoms);
        this.sleepHours = sleepHours;
    }

    Long getId() {
        return id;
    }

    LocalDate getDate() {
        return date;
    }

    CyclePhase getCyclePhase() {
        return cyclePhase;
    }

    EnergyLevel getEnergyLevel() {
        return energyLevel;
    }

    SleepQuality getSleepQuality() {
        return sleepQuality;
    }

    Set<Symptom> getSymptoms() {
        return Set.copyOf(symptoms);
    }

    double getSleepHours() {
        return sleepHours;
    }
}
