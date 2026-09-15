package com.alba.cycleruncoach.service;

import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.DailyCheckIn;
import com.alba.cycleruncoach.domain.EnergyLevel;
import com.alba.cycleruncoach.domain.SleepQuality;
import com.alba.cycleruncoach.domain.Symptom;
import com.alba.cycleruncoach.domain.TrainingRecommendation;
import com.alba.cycleruncoach.domain.WorkoutType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RecommendationServiceTest {

    private final RecommendationService recommendationService =
            new RecommendationService();

    @Test
    void rejectsNullDailyCheckIn() {
        assertThrows(
                IllegalArgumentException.class,
                () -> recommendationService.recommend(null)
        );
    }

    @Test
    void recommendsRecoveryRun_whenEnergyIsExhausted() {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(
                CyclePhase.FOLLICULAR,
                EnergyLevel.EXHAUSTED,
                SleepQuality.EXCELLENT,
                Set.of(),
                8.0
        );

        TrainingRecommendation recommendation =
                recommendationService.recommend(dailyCheckIn);

        assertEquals(
                WorkoutType.RECOVERY_RUN,
                recommendation.workoutType()
        );
        assertEquals(3, recommendation.maxPerceivedEffort());
        assertEquals(
                "Your energy seems very low today. Consider a gentle recovery run.",
                recommendation.reason()
        );
    }

    @Test
    void recommendsRecoveryRun_whenSleepIsBelowFiveHours() {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(
                CyclePhase.FOLLICULAR,
                EnergyLevel.HIGH,
                SleepQuality.GOOD,
                Set.of(),
                4.9
        );

        TrainingRecommendation recommendation =
                recommendationService.recommend(dailyCheckIn);

        assertEquals(
                WorkoutType.RECOVERY_RUN,
                recommendation.workoutType()
        );
        assertEquals(3, recommendation.maxPerceivedEffort());
        assertEquals(
                "You had very little sleep. A gentle recovery run may be the best option today.",
                recommendation.reason()
        );
    }

    @Test
    void recommendsEasyRun_whenSymptomsAreReported() {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(
                CyclePhase.OVULATORY,
                EnergyLevel.HIGH,
                SleepQuality.EXCELLENT,
                Set.of(Symptom.CRAMPS),
                8.0
        );

        TrainingRecommendation recommendation =
                recommendationService.recommend(dailyCheckIn);

        assertEquals(
                WorkoutType.EASY_RUN,
                recommendation.workoutType()
        );
        assertEquals(4, recommendation.maxPerceivedEffort());
        assertEquals(
                "You reported symptoms today. Consider an easy run and adjust the effort based on how you feel.",
                recommendation.reason()
        );
    }

    @ParameterizedTest
    @ValueSource(doubles = {5.0, 6.9})
    void recommendsEasyRun_whenSleepIsBetweenFiveAndSevenHours(
            double sleepHours
    ) {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(
                CyclePhase.FOLLICULAR,
                EnergyLevel.HIGH,
                SleepQuality.GOOD,
                Set.of(),
                sleepHours
        );

        TrainingRecommendation recommendation =
                recommendationService.recommend(dailyCheckIn);

        assertEquals(
                WorkoutType.EASY_RUN,
                recommendation.workoutType()
        );
        assertEquals(5, recommendation.maxPerceivedEffort());
        assertEquals(
                "You slept less than usual. An easy run at a comfortable effort could be a good choice.",
                recommendation.reason()
        );
    }

    @Test
    void recommendsEasyRun_whenEnergyIsLow() {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(
                CyclePhase.FOLLICULAR,
                EnergyLevel.LOW,
                SleepQuality.GOOD,
                Set.of(),
                8.0
        );

        TrainingRecommendation recommendation =
                recommendationService.recommend(dailyCheckIn);

        assertEquals(
                WorkoutType.EASY_RUN,
                recommendation.workoutType()
        );
        assertEquals(5, recommendation.maxPerceivedEffort());
        assertEquals(
                "Your energy or sleep suggests taking it easier today. A comfortable easy run may suit you.",
                recommendation.reason()
        );
    }

    @Test
    void recommendsEasyRun_whenSleepQualityIsBad() {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(
                CyclePhase.FOLLICULAR,
                EnergyLevel.HIGH,
                SleepQuality.BAD,
                Set.of(),
                8.0
        );

        TrainingRecommendation recommendation =
                recommendationService.recommend(dailyCheckIn);

        assertEquals(
                WorkoutType.EASY_RUN,
                recommendation.workoutType()
        );
        assertEquals(5, recommendation.maxPerceivedEffort());
        assertEquals(
                "Your energy or sleep suggests taking it easier today. A comfortable easy run may suit you.",
                recommendation.reason()
        );
    }

    @Test
    void recommendsEasyRun_whenEnergyIsMedium() {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(
                CyclePhase.FOLLICULAR,
                EnergyLevel.MEDIUM,
                SleepQuality.GOOD,
                Set.of(),
                8.0
        );

        TrainingRecommendation recommendation =
                recommendationService.recommend(dailyCheckIn);

        assertEquals(
                WorkoutType.EASY_RUN,
                recommendation.workoutType()
        );
        assertEquals(6, recommendation.maxPerceivedEffort());
        assertEquals(
                "Your readiness looks steady today. An easy run could help you maintain consistency.",
                recommendation.reason()
        );
    }

    @Test
    void recommendsEasyRun_whenSleepQualityIsOnlyOk() {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(
                CyclePhase.FOLLICULAR,
                EnergyLevel.HIGH,
                SleepQuality.OK,
                Set.of(),
                8.0
        );

        TrainingRecommendation recommendation =
                recommendationService.recommend(dailyCheckIn);

        assertEquals(
                WorkoutType.EASY_RUN,
                recommendation.workoutType()
        );
        assertEquals(6, recommendation.maxPerceivedEffort());
        assertEquals(
                "Your readiness looks steady today. An easy run could help you maintain consistency.",
                recommendation.reason()
        );
    }

    @ParameterizedTest(name = "{0} recommends {1}")
    @MethodSource("favorableRecommendations")
    void recommendsTrainingByCyclePhase_whenReadinessIsFavorable(
            CyclePhase cyclePhase,
            WorkoutType expectedWorkoutType,
            int expectedMaxEffort,
            String expectedReason
    ) {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(
                cyclePhase,
                EnergyLevel.HIGH,
                SleepQuality.GOOD,
                Set.of(),
                8.0
        );

        TrainingRecommendation recommendation =
                recommendationService.recommend(dailyCheckIn);

        assertEquals(
                expectedWorkoutType,
                recommendation.workoutType()
        );
        assertEquals(
                expectedMaxEffort,
                recommendation.maxPerceivedEffort()
        );
        assertEquals(expectedReason, recommendation.reason());
    }

    @Test
    void recommendsTrainingByCyclePhase_whenSleepIsExactlySevenHours() {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(
                CyclePhase.FOLLICULAR,
                EnergyLevel.VERY_HIGH,
                SleepQuality.EXCELLENT,
                Set.of(),
                7.0
        );

        TrainingRecommendation recommendation =
                recommendationService.recommend(dailyCheckIn);

        assertEquals(
                WorkoutType.TEMPO_RUN,
                recommendation.workoutType()
        );
        assertEquals(8, recommendation.maxPerceivedEffort());
        assertEquals(
                "You seem well recovered today. A tempo run could be a good option.",
                recommendation.reason()
        );
    }

    @Test
    void prioritizesExhaustion_overOtherReadinessSignals() {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(
                CyclePhase.OVULATORY,
                EnergyLevel.EXHAUSTED,
                SleepQuality.BAD,
                Set.of(Symptom.CRAMPS),
                4.0
        );

        TrainingRecommendation recommendation =
                recommendationService.recommend(dailyCheckIn);

        assertEquals(
                WorkoutType.RECOVERY_RUN,
                recommendation.workoutType()
        );
        assertEquals(3, recommendation.maxPerceivedEffort());
        assertEquals(
                "Your energy seems very low today. Consider a gentle recovery run.",
                recommendation.reason()
        );
    }

    @Test
    void prioritizesVeryLowSleep_overReportedSymptoms() {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(
                CyclePhase.OVULATORY,
                EnergyLevel.HIGH,
                SleepQuality.GOOD,
                Set.of(Symptom.CRAMPS),
                4.9
        );

        TrainingRecommendation recommendation =
                recommendationService.recommend(dailyCheckIn);

        assertEquals(
                WorkoutType.RECOVERY_RUN,
                recommendation.workoutType()
        );
        assertEquals(3, recommendation.maxPerceivedEffort());
        assertEquals(
                "You had very little sleep. A gentle recovery run may be the best option today.",
                recommendation.reason()
        );
    }

    @Test
    void prioritizesReportedSymptoms_overInsufficientSleep() {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(
                CyclePhase.FOLLICULAR,
                EnergyLevel.HIGH,
                SleepQuality.GOOD,
                Set.of(Symptom.CRAMPS),
                6.0
        );

        TrainingRecommendation recommendation =
                recommendationService.recommend(dailyCheckIn);

        assertEquals(
                WorkoutType.EASY_RUN,
                recommendation.workoutType()
        );
        assertEquals(4, recommendation.maxPerceivedEffort());
        assertEquals(
                "You reported symptoms today. Consider an easy run and adjust the effort based on how you feel.",
                recommendation.reason()
        );
    }

    private static Stream<Arguments> favorableRecommendations() {
        return Stream.of(
                Arguments.of(
                        CyclePhase.FOLLICULAR,
                        WorkoutType.TEMPO_RUN,
                        8,
                        "You seem well recovered today. A tempo run could be a good option."
                ),
                Arguments.of(
                        CyclePhase.OVULATORY,
                        WorkoutType.INTERVALS,
                        6,
                        "Your readiness looks good today. You could try an interval session."
                ),
                Arguments.of(
                        CyclePhase.LUTEAL,
                        WorkoutType.EASY_RUN,
                        6,
                        "Your readiness looks good today. A steady easy run could be a comfortable choice."
                ),
                Arguments.of(
                        CyclePhase.MENSTRUAL,
                        WorkoutType.EASY_RUN,
                        5,
                        "A gentle easy run may suit you today. Adjust the effort based on how you feel."
                )
        );
    }

    private DailyCheckIn createDailyCheckIn(
            CyclePhase cyclePhase,
            EnergyLevel energyLevel,
            SleepQuality sleepQuality,
            Set<Symptom> symptoms,
            double sleepHours
    ) {
        return new DailyCheckIn(
                1L,
                LocalDate.of(2026, 9, 15),
                cyclePhase,
                energyLevel,
                sleepQuality,
                symptoms,
                sleepHours
        );
    }
}