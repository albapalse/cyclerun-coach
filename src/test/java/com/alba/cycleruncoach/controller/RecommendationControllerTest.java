package com.alba.cycleruncoach.controller;

import com.alba.cycleruncoach.controller.mapper.RecommendationDtoMapper;
import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.DailyCheckIn;
import com.alba.cycleruncoach.domain.EnergyLevel;
import com.alba.cycleruncoach.domain.SleepQuality;
import com.alba.cycleruncoach.domain.TrainingRecommendation;
import com.alba.cycleruncoach.domain.WorkoutType;
import com.alba.cycleruncoach.service.DailyCheckInService;
import com.alba.cycleruncoach.service.RecommendationService;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendationController.class)
@Import(RecommendationDtoMapper.class)
class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DailyCheckInService dailyCheckInService;

    @MockitoBean
    private RecommendationService recommendationService;

    @Test
    void findLatestRecommendation_returnsRecommendation_whenCheckInExists()
            throws Exception {
        DailyCheckIn dailyCheckIn = new DailyCheckIn(
                1L,
                LocalDate.of(2026, 9, 16),
                CyclePhase.FOLLICULAR,
                EnergyLevel.HIGH,
                SleepQuality.GOOD,
                Set.of(),
                8.0
        );

        TrainingRecommendation recommendation =
                new TrainingRecommendation(
                        WorkoutType.TEMPO_RUN,
                        8,
                        "You seem well recovered today. "
                                + "A tempo run could be a good option."
                );

        when(dailyCheckInService.findLatestDailyCheckIn())
                .thenReturn(Optional.of(dailyCheckIn));
        when(recommendationService.recommend(dailyCheckIn))
                .thenReturn(recommendation);

        mockMvc.perform(get("/api/recommendations/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkInId").value(1))
                .andExpect(jsonPath("$.checkInDate")
                        .value("2026-09-16"))
                .andExpect(jsonPath("$.workoutType")
                        .value("TEMPO_RUN"))
                .andExpect(jsonPath("$.maxPerceivedEffort").value(8))
                .andExpect(jsonPath("$.reason")
                        .value(
                                "You seem well recovered today. "
                                        + "A tempo run could be a good option."
                        ));

        verify(dailyCheckInService).findLatestDailyCheckIn();
        verify(recommendationService).recommend(dailyCheckIn);
    }

    @Test
    void findLatestRecommendation_returnsNotFound_whenNoCheckInExists()
            throws Exception {
        when(dailyCheckInService.findLatestDailyCheckIn())
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/recommendations/latest"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value(
                        "No daily check-in was found yet. "
                                + "Add today's check-in first, and then we'll prepare "
                                + "your recommendation."
                ))
                .andExpect(jsonPath("$.path")
                        .value("/api/recommendations/latest"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verify(dailyCheckInService).findLatestDailyCheckIn();
        verify(recommendationService, never()).recommend(any());
    }
}
