package com.alba.cycleruncoach.controller;

import com.alba.cycleruncoach.controller.dto.RecommendationResponse;
import com.alba.cycleruncoach.controller.mapper.RecommendationDtoMapper;
import com.alba.cycleruncoach.domain.DailyCheckIn;
import com.alba.cycleruncoach.domain.TrainingRecommendation;
import com.alba.cycleruncoach.exception.ResourceNotFoundException;
import com.alba.cycleruncoach.service.DailyCheckInService;
import com.alba.cycleruncoach.service.RecommendationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final DailyCheckInService dailyCheckInService;
    private final RecommendationService recommendationService;
    private final RecommendationDtoMapper recommendationDtoMapper;

    public RecommendationController(
            DailyCheckInService dailyCheckInService,
            RecommendationService recommendationService,
            RecommendationDtoMapper recommendationDtoMapper
    ) {
        this.dailyCheckInService = dailyCheckInService;
        this.recommendationService = recommendationService;
        this.recommendationDtoMapper = recommendationDtoMapper;
    }

    @GetMapping("/latest")
    public ResponseEntity<RecommendationResponse> findLatestRecommendation() {
        DailyCheckIn dailyCheckIn = dailyCheckInService
                .findLatestDailyCheckIn()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No daily check-in was found yet. "
                                + "Add today's check-in first, and then we'll prepare "
                                + "your recommendation."
                ));

        TrainingRecommendation recommendation =
                recommendationService.recommend(dailyCheckIn);

        RecommendationResponse response =
                recommendationDtoMapper.toResponse(
                        dailyCheckIn,
                        recommendation
                );

        return ResponseEntity.ok(response);
    }
}
