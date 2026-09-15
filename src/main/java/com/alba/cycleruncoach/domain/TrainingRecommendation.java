package com.alba.cycleruncoach.domain;

public record TrainingRecommendation(
        WorkoutType workoutType,
        int maxPerceivedEffort,
        String reason
) {

    public TrainingRecommendation {
        if (workoutType == null) {
            throw new IllegalArgumentException(
                    "Workout type cannot be null"
            );
        }

        if (maxPerceivedEffort < 1 || maxPerceivedEffort > 10) {
            throw new IllegalArgumentException(
                    "Maximum perceived effort must be between 1 and 10"
            );
        }

        if (reason == null || reason.isBlank()) {
            throw new IllegalArgumentException(
                    "Reason cannot be blank"
            );
        }
    }
}