package com.alba.cycleruncoach.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrainingRecommendationTest {

    @Test
    void createsRecommendation_whenDataIsValid() {
        TrainingRecommendation recommendation =
                new TrainingRecommendation(
                        WorkoutType.TEMPO_RUN,
                        8,
                        "Readiness conditions are favorable"
                );

        assertEquals(
                WorkoutType.TEMPO_RUN,
                recommendation.workoutType()
        );
        assertEquals(8, recommendation.maxPerceivedEffort());
        assertEquals(
                "Readiness conditions are favorable",
                recommendation.reason()
        );
    }

    @Test
    void rejectsNullWorkoutType() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingRecommendation(
                        null,
                        5,
                        "Valid reason"
                )
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 11})
    void rejectsEffortOutsideValidRange(int effort) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingRecommendation(
                        WorkoutType.EASY_RUN,
                        effort,
                        "Valid reason"
                )
        );
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void rejectsMissingOrBlankReason(String reason) {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingRecommendation(
                        WorkoutType.EASY_RUN,
                        5,
                        reason
                )
        );
    }
}