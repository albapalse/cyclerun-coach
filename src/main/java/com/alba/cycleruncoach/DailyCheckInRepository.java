package com.alba.cycleruncoach;

import java.util.List;
import java.util.Optional;

public interface DailyCheckInRepository {
    void save(DailyCheckIn dailyCheckIn);
    List<DailyCheckIn> findAll();
    Optional<DailyCheckIn> findById(Long id);
    boolean deleteById(Long id);
}
