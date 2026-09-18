package com.alba.cycleruncoach.controller.mapper;

import com.alba.cycleruncoach.controller.dto.CreateDailyCheckInRequest;
import com.alba.cycleruncoach.controller.dto.DailyCheckInResponse;
import com.alba.cycleruncoach.controller.dto.UpdateDailyCheckInRequest;
import com.alba.cycleruncoach.domain.DailyCheckIn;

import org.springframework.stereotype.Component;

@Component
public class DailyCheckInDtoMapper {

    public DailyCheckIn toDomain(
            CreateDailyCheckInRequest request
    ) {
        return new DailyCheckIn(
                request.id(),
                request.date(),
                request.cyclePhase(),
                request.energyLevel(),
                request.sleepQuality(),
                request.symptoms(),
                request.sleepHours()
        );
    }

    public DailyCheckIn toDomain(
            Long id,
            UpdateDailyCheckInRequest request
    ) {
        return new DailyCheckIn(
                id,
                request.date(),
                request.cyclePhase(),
                request.energyLevel(),
                request.sleepQuality(),
                request.symptoms(),
                request.sleepHours()
        );
    }

    public DailyCheckInResponse toResponse(
            DailyCheckIn dailyCheckIn
    ) {
        return new DailyCheckInResponse(
                dailyCheckIn.getId(),
                dailyCheckIn.getDate(),
                dailyCheckIn.getCyclePhase(),
                dailyCheckIn.getEnergyLevel(),
                dailyCheckIn.getSleepQuality(),
                dailyCheckIn.getSymptoms(),
                dailyCheckIn.getSleepHours()
        );
    }
}
