package com.alba.cycleruncoach.service;

import com.alba.cycleruncoach.domain.DailyCheckIn;
import com.alba.cycleruncoach.repository.DailyCheckInRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class DailyCheckInService {

    private final DailyCheckInRepository dailyCheckInRepository;

    public DailyCheckInService(DailyCheckInRepository dailyCheckInRepository) {
        validateRepository(dailyCheckInRepository);
        this.dailyCheckInRepository = dailyCheckInRepository;
    }

    public void saveDailyCheckIn(DailyCheckIn dailyCheckIn) {
        dailyCheckInRepository.save(dailyCheckIn);
    }

    public List<DailyCheckIn> findAllDailyCheckIns() {
        return dailyCheckInRepository.findAll();
    }

    public Optional<DailyCheckIn> findDailyCheckInById(Long id) {
        return dailyCheckInRepository.findById(id);
    }

    public boolean updateDailyCheckIn(DailyCheckIn dailyCheckIn) {
        return dailyCheckInRepository.update(dailyCheckIn);
    }

    public boolean deleteDailyCheckInById(Long id) {
        return dailyCheckInRepository.deleteById(id);
    }

    public Optional<DailyCheckIn> findLatestDailyCheckIn() {
        return dailyCheckInRepository.findAll()
                .stream()
                .max(Comparator.comparing(DailyCheckIn::getDate)
                        .thenComparing(DailyCheckIn::getId));
    }

    private void validateRepository(DailyCheckInRepository dailyCheckInRepository) {
        if (dailyCheckInRepository == null) {
            throw new IllegalArgumentException("Daily check-in repository cannot be null");
        }
    }
}
