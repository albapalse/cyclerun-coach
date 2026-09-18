package com.alba.cycleruncoach.service;

import com.alba.cycleruncoach.domain.DailyCheckIn;
import com.alba.cycleruncoach.domain.EnergyLevel;
import com.alba.cycleruncoach.domain.SleepQuality;
import com.alba.cycleruncoach.domain.TrainingRecommendation;
import com.alba.cycleruncoach.domain.WorkoutType;

import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

    public TrainingRecommendation recommend(DailyCheckIn dailyCheckIn) {
        if (dailyCheckIn == null) {
            throw new IllegalArgumentException(
                    "Daily check-in cannot be null"
            );
        }

        // Rules are ordered by priority, so the first match determines
        // the recommendation.
        if (dailyCheckIn.getEnergyLevel() == EnergyLevel.EXHAUSTED) {
            return new TrainingRecommendation(
                    WorkoutType.RECOVERY_RUN,
                    3,
                    "You reported very low energy today. Consider a gentle recovery run."
            );
        }

        if (dailyCheckIn.getSleepHours() < 5) {
            return new TrainingRecommendation(
                    WorkoutType.RECOVERY_RUN,
                    3,
                    "You reported fewer than five hours of sleep. A gentle recovery run may be a good option today."
            );
        }

        if (!dailyCheckIn.getSymptoms().isEmpty()) {
            return new TrainingRecommendation(
                    WorkoutType.EASY_RUN,
                    4,
                    "You reported symptoms today. Consider an easy run and adjust the effort based on how you feel."
            );
        }

        if (dailyCheckIn.getSleepHours() < 7) {
            return new TrainingRecommendation(
                    WorkoutType.EASY_RUN,
                    5,
                    "You slept less than usual. An easy run at a comfortable effort could be a good choice."
            );
        }

        if (dailyCheckIn.getEnergyLevel() == EnergyLevel.LOW) {
            return new TrainingRecommendation(
                    WorkoutType.EASY_RUN,
                    5,
                    "You reported low energy today. An easy run at a comfortable effort may suit you."
            );
        }

        if (dailyCheckIn.getSleepQuality() == SleepQuality.BAD) {
            return new TrainingRecommendation(
                    WorkoutType.EASY_RUN,
                    5,
                    "You reported poor sleep quality. An easy run at a comfortable effort may be a good option today."
            );
        }

        if (isFavorable(dailyCheckIn)) {
            return switch (dailyCheckIn.getCyclePhase()) {
                case FOLLICULAR -> new TrainingRecommendation(
                        WorkoutType.TEMPO_RUN,
                        8,
                        "You seem well recovered today. A tempo run could be a good option."
                );
                case OVULATORY -> new TrainingRecommendation(
                        WorkoutType.INTERVALS,
                        6,
                        "Your readiness looks good today. You could try an interval session."
                );
                case LUTEAL -> new TrainingRecommendation(
                        WorkoutType.EASY_RUN,
                        6,
                        "Your readiness looks good today. A steady easy run could be a comfortable choice."
                );
                case MENSTRUAL -> new TrainingRecommendation(
                        WorkoutType.EASY_RUN,
                        5,
                        "A gentle easy run may suit you today. Adjust the effort based on how you feel."
                );
            };
        }

        return new TrainingRecommendation(
                WorkoutType.EASY_RUN,
                6,
                "Your check-in suggests keeping the effort comfortable today. An easy run could be a good option."
        );
    }

    private boolean isFavorable(DailyCheckIn dailyCheckIn) {
        boolean hasHighEnergy =
                dailyCheckIn.getEnergyLevel() == EnergyLevel.HIGH
                        || dailyCheckIn.getEnergyLevel() == EnergyLevel.VERY_HIGH;

        boolean hasGoodSleepQuality =
                dailyCheckIn.getSleepQuality() == SleepQuality.GOOD
                        || dailyCheckIn.getSleepQuality() == SleepQuality.EXCELLENT;

        return hasHighEnergy
                && hasGoodSleepQuality
                && dailyCheckIn.getSleepHours() >= 7
                && dailyCheckIn.getSymptoms().isEmpty();
    }
}
