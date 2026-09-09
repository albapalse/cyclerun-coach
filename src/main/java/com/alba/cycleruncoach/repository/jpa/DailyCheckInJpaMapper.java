package com.alba.cycleruncoach.repository.jpa;

import com.alba.cycleruncoach.domain.DailyCheckIn;

final class DailyCheckInJpaMapper {

    private DailyCheckInJpaMapper() {
    }

    static DailyCheckInJpaEntity toEntity(DailyCheckIn dailyCheckIn) {
        return new DailyCheckInJpaEntity(
                dailyCheckIn.getId(),
                dailyCheckIn.getDate(),
                dailyCheckIn.getCyclePhase(),
                dailyCheckIn.getEnergyLevel(),
                dailyCheckIn.getSleepQuality(),
                dailyCheckIn.getSymptoms(),
                dailyCheckIn.getSleepHours()
        );
    }

    static DailyCheckIn toDomain(DailyCheckInJpaEntity entity) {
        return new DailyCheckIn(
                entity.getId(),
                entity.getDate(),
                entity.getCyclePhase(),
                entity.getEnergyLevel(),
                entity.getSleepQuality(),
                entity.getSymptoms(),
                entity.getSleepHours()
        );
    }
}
