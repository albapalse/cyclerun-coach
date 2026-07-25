package com.alba.cycleruncoach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DailyCheckInServiceTest {

    private DailyCheckInRepository dailyCheckInRepository;
    private DailyCheckInService dailyCheckInService;

    @BeforeEach
    void setUp() {
        dailyCheckInRepository =
                new InMemoryDailyCheckInRepository();

        dailyCheckInService =
                new DailyCheckInService(dailyCheckInRepository);
    }

    @Test
    void constructor_throwsException_whenRepositoryIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new DailyCheckInService(null)
        );
    }

    @Test
    void saveDailyCheckIn_storesDailyCheckInInRepository() {
        DailyCheckIn checkIn = createDailyCheckIn(1L);

        dailyCheckInService.saveDailyCheckIn(checkIn);

        DailyCheckIn storedCheckIn = dailyCheckInRepository
                .findById(1L)
                .orElseThrow();

        assertSame(checkIn, storedCheckIn);
    }

    @Test
    void saveDailyCheckIn_throwsException_whenDailyCheckInIsNull() {
        assertThrows(
                IllegalArgumentException.class,
                () -> dailyCheckInService.saveDailyCheckIn(null)
        );
    }

    @Test
    void findAllDailyCheckIns_returnsSavedDailyCheckIns() {
        DailyCheckIn firstCheckIn = createDailyCheckIn(1L);
        DailyCheckIn secondCheckIn = createDailyCheckIn(2L);

        dailyCheckInService.saveDailyCheckIn(firstCheckIn);
        dailyCheckInService.saveDailyCheckIn(secondCheckIn);

        List<DailyCheckIn> result =
                dailyCheckInService.findAllDailyCheckIns();

        assertEquals(2, result.size());
        assertTrue(result.contains(firstCheckIn));
        assertTrue(result.contains(secondCheckIn));
    }

    @Test
    void findDailyCheckInById_returnsDailyCheckIn_whenItExists() {
        DailyCheckIn checkIn = createDailyCheckIn(1L);
        dailyCheckInService.saveDailyCheckIn(checkIn);

        Optional<DailyCheckIn> result =
                dailyCheckInService.findDailyCheckInById(1L);

        assertTrue(result.isPresent());
        assertSame(checkIn, result.orElseThrow());
    }

    @Test
    void findDailyCheckInById_returnsEmpty_whenItDoesNotExist() {
        Optional<DailyCheckIn> result =
                dailyCheckInService.findDailyCheckInById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteDailyCheckInById_deletesDailyCheckIn_whenItExists() {
        DailyCheckIn checkIn = createDailyCheckIn(1L);
        dailyCheckInService.saveDailyCheckIn(checkIn);

        boolean deleted =
                dailyCheckInService.deleteDailyCheckInById(1L);

        assertTrue(deleted);
        assertTrue(
                dailyCheckInService
                        .findDailyCheckInById(1L)
                        .isEmpty()
        );
    }

    @Test
    void deleteDailyCheckInById_returnsFalse_whenItDoesNotExist() {
        boolean deleted =
                dailyCheckInService.deleteDailyCheckInById(99L);

        assertFalse(deleted);
    }

    @Test
    void findLatestDailyCheckIn_returnsMostRecentCheckIn() {
        DailyCheckIn oldestCheckIn = createDailyCheckIn(
                1L,
                LocalDate.of(2026, 7, 20)
        );

        DailyCheckIn latestCheckIn = createDailyCheckIn(
                2L,
                LocalDate.of(2026, 7, 24)
        );

        DailyCheckIn middleCheckIn = createDailyCheckIn(
                3L,
                LocalDate.of(2026, 7, 22)
        );

        // Deliberately saved out of chronological order.
        dailyCheckInService.saveDailyCheckIn(latestCheckIn);
        dailyCheckInService.saveDailyCheckIn(oldestCheckIn);
        dailyCheckInService.saveDailyCheckIn(middleCheckIn);

        Optional<DailyCheckIn> result =
                dailyCheckInService.findLatestDailyCheckIn();

        assertTrue(result.isPresent());
        assertSame(latestCheckIn, result.orElseThrow());
    }

    @Test
    void findLatestDailyCheckIn_returnsEmpty_whenNoCheckInsExist() {
        Optional<DailyCheckIn> result =
                dailyCheckInService.findLatestDailyCheckIn();

        assertTrue(result.isEmpty());
    }

    private DailyCheckIn createDailyCheckIn(Long id) {
        return createDailyCheckIn(
                id,
                LocalDate.of(2026, 7, 24)
        );
    }

    private DailyCheckIn createDailyCheckIn(
            Long id,
            LocalDate date
    ) {
        return new DailyCheckIn(
                id,
                date,
                CyclePhase.LUTEAL,
                EnergyLevel.LOW,
                SleepQuality.GOOD,
                Set.of(Symptom.FATIGUE),
                7.5
        );
    }
}