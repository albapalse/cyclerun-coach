package com.alba.cycleruncoach.repository.jpa;

import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.DailyCheckIn;
import com.alba.cycleruncoach.domain.EnergyLevel;
import com.alba.cycleruncoach.domain.SleepQuality;
import com.alba.cycleruncoach.domain.Symptom;
import com.alba.cycleruncoach.repository.DailyCheckInRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(JpaDailyCheckInRepositoryAdapter.class)
class JpaDailyCheckInRepositoryAdapterTest {

    @Autowired
    private DailyCheckInRepository repository;

    @Test
    void shouldSaveAndFindDailyCheckInWithSymptoms() {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(1L);

        repository.save(dailyCheckIn);

        DailyCheckIn stored = repository.findById(1L).orElseThrow();

        assertDailyCheckInEquals(dailyCheckIn, stored);
    }

    @Test
    void shouldPersistEmptySymptoms() {
        DailyCheckIn dailyCheckIn = new DailyCheckIn(
                1L,
                LocalDate.of(2026, 9, 9),
                CyclePhase.MENSTRUAL,
                EnergyLevel.EXHAUSTED,
                SleepQuality.BAD,
                Set.of(),
                0.0
        );

        repository.save(dailyCheckIn);

        DailyCheckIn stored = repository.findById(1L).orElseThrow();

        assertDailyCheckInEquals(dailyCheckIn, stored);
        assertTrue(stored.getSymptoms().isEmpty());
    }

    @Test
    void shouldPersistEnumNamesAndUpperSleepBoundary() {
        DailyCheckIn dailyCheckIn = new DailyCheckIn(
                1L,
                LocalDate.of(2026, 9, 9),
                CyclePhase.OVULATORY,
                EnergyLevel.VERY_HIGH,
                SleepQuality.EXCELLENT,
                Set.of(Symptom.MUSCLE_SORENESS, Symptom.STOMACH_ACHE),
                24.0
        );

        repository.save(dailyCheckIn);

        DailyCheckIn stored = repository.findById(1L).orElseThrow();

        assertDailyCheckInEquals(dailyCheckIn, stored);
    }

    @Test
    void shouldFindAllDailyCheckIns() {
        DailyCheckIn first = createDailyCheckIn(1L);
        DailyCheckIn second = new DailyCheckIn(
                2L,
                LocalDate.of(2026, 9, 10),
                CyclePhase.LUTEAL,
                EnergyLevel.LOW,
                SleepQuality.OK,
                Set.of(Symptom.HEADACHE),
                6.5
        );
        repository.save(first);
        repository.save(second);

        List<DailyCheckIn> stored = repository.findAll();

        assertEquals(2, stored.size());
        assertTrue(stored.stream().anyMatch(item -> item.getId().equals(1L)));
        assertTrue(stored.stream().anyMatch(item -> item.getId().equals(2L)));
    }

    @Test
    void shouldReturnEmptyWhenDailyCheckInDoesNotExist() {
        Optional<DailyCheckIn> stored = repository.findById(999L);

        assertTrue(stored.isEmpty());
    }

    @Test
    void shouldRejectDuplicateId() {
        repository.save(createDailyCheckIn(1L));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(createDailyCheckIn(1L))
        );

        assertEquals(
                "A daily check-in with ID 1 already exists. Please use a different ID.",
                exception.getMessage()
        );
    }

    @Test
    void shouldUpdateExistingDailyCheckInAndItsSymptoms() {
        repository.save(createDailyCheckIn(1L));
        DailyCheckIn updated = new DailyCheckIn(
                1L,
                LocalDate.of(2026, 9, 11),
                CyclePhase.LUTEAL,
                EnergyLevel.HIGH,
                SleepQuality.EXCELLENT,
                Set.of(Symptom.BLOATING),
                9.0
        );

        boolean wasUpdated = repository.update(updated);
        DailyCheckIn stored = repository.findById(1L).orElseThrow();

        assertTrue(wasUpdated);
        assertDailyCheckInEquals(updated, stored);
        assertEquals(Set.of(Symptom.BLOATING), stored.getSymptoms());
    }

    @Test
    void shouldReturnFalseWhenUpdatingMissingDailyCheckIn() {
        boolean wasUpdated = repository.update(createDailyCheckIn(99L));

        assertFalse(wasUpdated);
        assertTrue(repository.findById(99L).isEmpty());
    }

    @Test
    void shouldDeleteExistingDailyCheckInAndItsSymptoms() {
        repository.save(createDailyCheckIn(1L));

        boolean wasDeleted = repository.deleteById(1L);

        assertTrue(wasDeleted);
        assertTrue(repository.findById(1L).isEmpty());
    }

    @Test
    void shouldReturnFalseWhenDeletingMissingDailyCheckIn() {
        assertFalse(repository.deleteById(99L));
    }

    @Test
    void shouldRejectNullDailyCheckIn() {
        assertThrows(
                IllegalArgumentException.class,
                () -> repository.save(null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> repository.update(null)
        );
    }

    @Test
    void shouldRejectInvalidIds() {
        assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById(null)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> repository.findById(0L)
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> repository.deleteById(-1L)
        );
    }

    private DailyCheckIn createDailyCheckIn(Long id) {
        return new DailyCheckIn(
                id,
                LocalDate.of(2026, 9, 9),
                CyclePhase.FOLLICULAR,
                EnergyLevel.MEDIUM,
                SleepQuality.GOOD,
                Set.of(Symptom.CRAMPS, Symptom.FATIGUE),
                7.5
        );
    }

    private void assertDailyCheckInEquals(
            DailyCheckIn expected,
            DailyCheckIn actual
    ) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getCyclePhase(), actual.getCyclePhase());
        assertEquals(expected.getEnergyLevel(), actual.getEnergyLevel());
        assertEquals(expected.getSleepQuality(), actual.getSleepQuality());
        assertEquals(expected.getSymptoms(), actual.getSymptoms());
        assertEquals(expected.getSleepHours(), actual.getSleepHours());
    }
}
