package com.alba.cycleruncoach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryDailyCheckInRepository implements DailyCheckInRepository {
    private final Map<Long, DailyCheckIn> dailyCheckIns;

    public InMemoryDailyCheckInRepository() {
        this.dailyCheckIns = new HashMap<>();
    }

    @Override
    public void save(DailyCheckIn dailyCheckIn) {
        validateDailyCheckIn(dailyCheckIn);
        if (dailyCheckIns.containsKey(dailyCheckIn.getId())) {
            throw new IllegalArgumentException("DailyCheckIn with id " + dailyCheckIn.getId() + " already exists");
        }
        dailyCheckIns.put(dailyCheckIn.getId(), dailyCheckIn);
    }

    @Override
    public List<DailyCheckIn> findAll() {
        return new ArrayList<>(dailyCheckIns.values());
    }

    @Override
    public Optional<DailyCheckIn> findById(Long id) {
        validateId(id);
        return Optional.ofNullable(dailyCheckIns.get(id));
    }

    @Override
    public boolean deleteById(Long id) {
        validateId(id);
        return dailyCheckIns.remove(id) != null;
    }

    private void validateDailyCheckIn(DailyCheckIn dailyCheckIn) {
        if (dailyCheckIn == null) {
            throw new IllegalArgumentException("DailyCheckIn cannot be null");
        }
    }

    private void validateId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("DailyCheckIn id must be greater than zero");
        }
    }
}
