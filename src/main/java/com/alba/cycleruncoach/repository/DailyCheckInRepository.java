package com.alba.cycleruncoach.repository;

import com.alba.cycleruncoach.domain.DailyCheckIn;

import java.util.List;
import java.util.Optional;

public interface DailyCheckInRepository {

    void save(DailyCheckIn dailyCheckIn);

    List<DailyCheckIn> findAll();

    Optional<DailyCheckIn> findById(Long id);

    boolean update(DailyCheckIn dailyCheckIn);

    boolean deleteById(Long id);
}
