package com.alba.cycleruncoach;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DailyCheckInTest {

    private static final LocalDate VALID_DATE =
            LocalDate.of(2026, 7, 22);

    private static final CyclePhase VALID_CYCLE_PHASE =
            CyclePhase.LUTEAL;

    private static final EnergyLevel VALID_ENERGY_LEVEL =
            EnergyLevel.LOW;

    private static final SleepQuality VALID_SLEEP_QUALITY =
            SleepQuality.GOOD;

    private static final Set<Symptom> VALID_SYMPTOMS =
            Set.of(Symptom.FATIGUE, Symptom.CRAMPS);

    private static final double VALID_SLEEP_HOURS = 7.5;

    @Test
    void constructor_createsDailyCheckIn_whenDataIsValid() {
        DailyCheckIn checkIn = createCheckIn(
                1L,
                VALID_DATE,
                VALID_CYCLE_PHASE,
                VALID_ENERGY_LEVEL,
                VALID_SLEEP_QUALITY,
                VALID_SYMPTOMS,
                VALID_SLEEP_HOURS
        );

        assertEquals(1L, checkIn.getId());
        assertEquals(VALID_DATE, checkIn.getDate());
        assertEquals(VALID_CYCLE_PHASE, checkIn.getCyclePhase());
        assertEquals(VALID_ENERGY_LEVEL, checkIn.getEnergyLevel());
        assertEquals(VALID_SLEEP_QUALITY, checkIn.getSleepQuality());
        assertEquals(VALID_SLEEP_HOURS, checkIn.getSleepHours());
        assertEquals(VALID_SYMPTOMS, checkIn.getSymptoms());
    }

    @Test
    void constructor_throwsException_whenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                createCheckIn(
                        null,
                        VALID_DATE,
                        VALID_CYCLE_PHASE,
                        VALID_ENERGY_LEVEL,
                        VALID_SLEEP_QUALITY,
                        VALID_SYMPTOMS,
                        VALID_SLEEP_HOURS
                )
        );
    }

    @Test
    void constructor_throwsException_whenIdIsZero() {
        assertThrows(IllegalArgumentException.class, () ->
                createCheckIn(
                        0L,
                        VALID_DATE,
                        VALID_CYCLE_PHASE,
                        VALID_ENERGY_LEVEL,
                        VALID_SLEEP_QUALITY,
                        VALID_SYMPTOMS,
                        VALID_SLEEP_HOURS
                )
        );
    }

    @Test
    void constructor_throwsException_whenIdIsNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                createCheckIn(
                        -1L,
                        VALID_DATE,
                        VALID_CYCLE_PHASE,
                        VALID_ENERGY_LEVEL,
                        VALID_SLEEP_QUALITY,
                        VALID_SYMPTOMS,
                        VALID_SLEEP_HOURS
                )
        );
    }

    @Test
    void constructor_throwsException_whenDateIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                createCheckIn(
                        1L,
                        null,
                        VALID_CYCLE_PHASE,
                        VALID_ENERGY_LEVEL,
                        VALID_SLEEP_QUALITY,
                        VALID_SYMPTOMS,
                        VALID_SLEEP_HOURS
                )
        );
    }

    @Test
    void constructor_throwsException_whenCyclePhaseIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                createCheckIn(
                        1L,
                        VALID_DATE,
                        null,
                        VALID_ENERGY_LEVEL,
                        VALID_SLEEP_QUALITY,
                        VALID_SYMPTOMS,
                        VALID_SLEEP_HOURS
                )
        );
    }

    @Test
    void constructor_throwsException_whenEnergyLevelIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                createCheckIn(
                        1L,
                        VALID_DATE,
                        VALID_CYCLE_PHASE,
                        null,
                        VALID_SLEEP_QUALITY,
                        VALID_SYMPTOMS,
                        VALID_SLEEP_HOURS
                )
        );
    }

    @Test
    void constructor_throwsException_whenSleepQualityIsNull() {
        assertThrows(IllegalArgumentException.class, () ->
                createCheckIn(
                        1L,
                        VALID_DATE,
                        VALID_CYCLE_PHASE,
                        VALID_ENERGY_LEVEL,
                        null,
                        VALID_SYMPTOMS,
                        VALID_SLEEP_HOURS
                )
        );
    }

    @Test
    void constructor_throwsException_whenSymptomsAreNull() {
        assertThrows(IllegalArgumentException.class, () ->
                createCheckIn(
                        1L,
                        VALID_DATE,
                        VALID_CYCLE_PHASE,
                        VALID_ENERGY_LEVEL,
                        VALID_SLEEP_QUALITY,
                        null,
                        VALID_SLEEP_HOURS
                )
        );
    }

    @Test
    void constructor_throwsException_whenSymptomsContainNull() {
        Set<Symptom> symptoms = new HashSet<>();
        symptoms.add(Symptom.FATIGUE);
        symptoms.add(null);

        assertThrows(IllegalArgumentException.class, () ->
                createCheckIn(
                        1L,
                        VALID_DATE,
                        VALID_CYCLE_PHASE,
                        VALID_ENERGY_LEVEL,
                        VALID_SLEEP_QUALITY,
                        symptoms,
                        VALID_SLEEP_HOURS
                )
        );
    }

    @Test
    void constructor_acceptsEmptySymptoms() {
        DailyCheckIn checkIn = createCheckIn(
                1L,
                VALID_DATE,
                VALID_CYCLE_PHASE,
                VALID_ENERGY_LEVEL,
                VALID_SLEEP_QUALITY,
                Set.of(),
                VALID_SLEEP_HOURS
        );

        assertTrue(checkIn.getSymptoms().isEmpty());
    }

    @Test
    void constructor_throwsException_whenSleepHoursAreNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                createCheckIn(
                        1L,
                        VALID_DATE,
                        VALID_CYCLE_PHASE,
                        VALID_ENERGY_LEVEL,
                        VALID_SLEEP_QUALITY,
                        VALID_SYMPTOMS,
                        -0.5
                )
        );
    }

    @Test
    void constructor_throwsException_whenSleepHoursAreGreaterThan24() {
        assertThrows(IllegalArgumentException.class, () ->
                createCheckIn(
                        1L,
                        VALID_DATE,
                        VALID_CYCLE_PHASE,
                        VALID_ENERGY_LEVEL,
                        VALID_SLEEP_QUALITY,
                        VALID_SYMPTOMS,
                        24.5
                )
        );
    }

    @Test
    void constructor_acceptsZeroSleepHours() {
        DailyCheckIn checkIn = createCheckIn(
                1L,
                VALID_DATE,
                VALID_CYCLE_PHASE,
                VALID_ENERGY_LEVEL,
                VALID_SLEEP_QUALITY,
                VALID_SYMPTOMS,
                0.0
        );

        assertEquals(0.0, checkIn.getSleepHours());
    }

    @Test
    void constructor_accepts24SleepHours() {
        DailyCheckIn checkIn = createCheckIn(
                1L,
                VALID_DATE,
                VALID_CYCLE_PHASE,
                VALID_ENERGY_LEVEL,
                VALID_SLEEP_QUALITY,
                VALID_SYMPTOMS,
                24.0
        );

        assertEquals(24.0, checkIn.getSleepHours());
    }

    @Test
    void hasSymptom_returnsTrue_whenSymptomExists() {
        DailyCheckIn checkIn = createValidCheckIn();

        assertTrue(checkIn.hasSymptom(Symptom.FATIGUE));
    }

    @Test
    void hasSymptom_returnsFalse_whenSymptomDoesNotExist() {
        DailyCheckIn checkIn = createValidCheckIn();

        assertFalse(checkIn.hasSymptom(Symptom.HEADACHE));
    }

    @Test
    void hasSymptom_throwsException_whenSymptomIsNull() {
        DailyCheckIn checkIn = createValidCheckIn();

        assertThrows(
                IllegalArgumentException.class,
                () -> checkIn.hasSymptom(null)
        );
    }

    @Test
    void constructor_copiesSymptomsDefensively() {
        Set<Symptom> originalSymptoms = new HashSet<>();
        originalSymptoms.add(Symptom.FATIGUE);

        DailyCheckIn checkIn = createCheckIn(
                1L,
                VALID_DATE,
                VALID_CYCLE_PHASE,
                VALID_ENERGY_LEVEL,
                VALID_SLEEP_QUALITY,
                originalSymptoms,
                VALID_SLEEP_HOURS
        );

        originalSymptoms.add(Symptom.CRAMPS);

        assertTrue(checkIn.hasSymptom(Symptom.FATIGUE));
        assertFalse(checkIn.hasSymptom(Symptom.CRAMPS));
    }

    @Test
    void getSymptoms_returnsUnmodifiableSet() {
        DailyCheckIn checkIn = createValidCheckIn();

        Set<Symptom> returnedSymptoms = checkIn.getSymptoms();

        assertThrows(
                UnsupportedOperationException.class,
                () -> returnedSymptoms.add(Symptom.HEADACHE)
        );
    }

    private DailyCheckIn createValidCheckIn() {
        return createCheckIn(
                1L,
                VALID_DATE,
                VALID_CYCLE_PHASE,
                VALID_ENERGY_LEVEL,
                VALID_SLEEP_QUALITY,
                VALID_SYMPTOMS,
                VALID_SLEEP_HOURS
        );
    }

    private DailyCheckIn createCheckIn(
            Long id,
            LocalDate date,
            CyclePhase cyclePhase,
            EnergyLevel energyLevel,
            SleepQuality sleepQuality,
            Set<Symptom> symptoms,
            double sleepHours
    ) {
        return new DailyCheckIn(
                id,
                date,
                cyclePhase,
                energyLevel,
                sleepQuality,
                symptoms,
                sleepHours
        );
    }
}