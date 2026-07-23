package com.alba.cycleruncoach;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryDailyCheckInRepositoryTest {

    private DailyCheckInRepository dailyCheckInRepository;

    @BeforeEach
    void setUp() {
        dailyCheckInRepository = new InMemoryDailyCheckInRepository();
    }

    private DailyCheckIn createDailyCheckIn(Long id) {
        return new DailyCheckIn(
                id,
                LocalDate.of(2026, 7, 23),
                CyclePhase.LUTEAL,
                EnergyLevel.LOW,
                SleepQuality.GOOD,
                Set.of(Symptom.FATIGUE),
                7.5
        );
    }
    
    @Test
    void save_savesDailyCheckIn() {
        DailyCheckIn checkIn = createDailyCheckIn(1L);
        dailyCheckInRepository.save(checkIn);

        Optional<DailyCheckIn> result = dailyCheckInRepository.findById(1L);

        assertTrue(result.isPresent());
        assertSame(checkIn, result.orElseThrow());
    }

    @Test
    void save_throwsException_whenNullDailyCheckIn() {
        assertThrows(IllegalArgumentException.class, () -> dailyCheckInRepository.save(null));
    }

    @Test
    void save_throwsException_whenIdAlreadyExists() {
        DailyCheckIn firstCheckIn = createDailyCheckIn(1L);
        DailyCheckIn secondCheckIn = createDailyCheckIn(1L);
        dailyCheckInRepository.save(firstCheckIn);

        assertThrows(IllegalArgumentException.class, () -> dailyCheckInRepository.save(secondCheckIn));
    }

    @Test
    void findById_returnsEmpty_whenDailyCheckInDoesNotExist() {
        Optional<DailyCheckIn> result = dailyCheckInRepository.findById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void findById_throwsException_whenNullId() {
        assertThrows(IllegalArgumentException.class, () -> dailyCheckInRepository.findById(null));
    }

    @Test
    void findById_throwsException_whenIdIsZero() {
        assertThrows(IllegalArgumentException.class, () -> dailyCheckInRepository.findById(0L));
    }

    @Test
    void findById_throwsException_whenIdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> dailyCheckInRepository.findById(-1L));
    }

    @Test
    void findAll_returnsEmpty_whenDailyCheckInDoesNotExist() {
        List<DailyCheckIn> result = dailyCheckInRepository.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_returnsAllSavedDailyCheckIns() {
        DailyCheckIn firstCheckIn = createDailyCheckIn(1L);
        DailyCheckIn secondCheckIn = createDailyCheckIn(2L);

        dailyCheckInRepository.save(firstCheckIn);
        dailyCheckInRepository.save(secondCheckIn);

        List<DailyCheckIn> result = dailyCheckInRepository.findAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(firstCheckIn));
        assertTrue(result.contains(secondCheckIn));
    }

    @Test
    void findAll_returnsCopyOfInternalCollection() {
        DailyCheckIn checkIn = createDailyCheckIn(1L);
        dailyCheckInRepository.save(checkIn);

        List<DailyCheckIn> result = dailyCheckInRepository.findAll();
        result.clear();

        List<DailyCheckIn> storedCheckIns = dailyCheckInRepository.findAll();

        assertEquals(1, storedCheckIns.size());
        assertTrue(storedCheckIns.contains(checkIn));
    }

    @Test
    void deleteById_deletesDailyCheckIn_whenItExists() {
        DailyCheckIn checkIn = createDailyCheckIn(1L);
        dailyCheckInRepository.save(checkIn);

        boolean deleted = dailyCheckInRepository.deleteById(1L);

        assertTrue(deleted);
        assertTrue(dailyCheckInRepository.findById(1L).isEmpty());
    }

    @Test
    void deleteById_returnsFalse_whenDailyCheckInDoesNotExist() {
        boolean deleted = dailyCheckInRepository.deleteById(99L);

        assertFalse(deleted);
    }

    @Test
    void deleteById_throwsException_whenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> dailyCheckInRepository.deleteById(null));
    }

    @Test
    void deleteById_throwsException_whenIdIsZero() {
        assertThrows(IllegalArgumentException.class, () -> dailyCheckInRepository.deleteById(0L));
    }

    @Test
    void deleteById_throwsException_whenIdIsNegative() {
        assertThrows(IllegalArgumentException.class, () -> dailyCheckInRepository.deleteById(-1L));
    }
}
