package com.alba.cycleruncoach.controller.mapper;

import com.alba.cycleruncoach.controller.dto.RecommendationResponse;
import com.alba.cycleruncoach.domain.DailyCheckIn;
import com.alba.cycleruncoach.domain.TrainingRecommendation;

import org.springframework.stereotype.Component;

@Component
public class RecommendationDtoMapper {

    public RecommendationResponse toResponse(
            DailyCheckIn dailyCheckIn,
            TrainingRecommendation recommendation
    ) {
        return new RecommendationResponse(
                dailyCheckIn.getId(),
                dailyCheckIn.getDate(),
                recommendation.workoutType(),
                recommendation.maxPerceivedEffort(),
                recommendation.reason()
        );
    }
}
