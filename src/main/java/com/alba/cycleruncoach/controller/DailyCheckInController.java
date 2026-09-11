package com.alba.cycleruncoach.controller;

import com.alba.cycleruncoach.controller.dto.CreateDailyCheckInRequest;
import com.alba.cycleruncoach.controller.dto.DailyCheckInResponse;
import com.alba.cycleruncoach.controller.dto.UpdateDailyCheckInRequest;
import com.alba.cycleruncoach.controller.mapper.DailyCheckInDtoMapper;
import com.alba.cycleruncoach.domain.DailyCheckIn;
import com.alba.cycleruncoach.service.DailyCheckInService;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/check-ins")
public class DailyCheckInController {

    private final DailyCheckInService dailyCheckInService;
    private final DailyCheckInDtoMapper dailyCheckInDtoMapper;

    public DailyCheckInController(
            DailyCheckInService dailyCheckInService,
            DailyCheckInDtoMapper dailyCheckInDtoMapper
    ) {
        this.dailyCheckInService = dailyCheckInService;
        this.dailyCheckInDtoMapper = dailyCheckInDtoMapper;
    }

    @GetMapping
    public List<DailyCheckInResponse> findAllDailyCheckIns() {
        return dailyCheckInService
                .findAllDailyCheckIns()
                .stream()
                .map(dailyCheckInDtoMapper::toResponse)
                .toList();
    }

    @GetMapping("/latest")
    public ResponseEntity<DailyCheckInResponse> findLatestDailyCheckIn() {
        return dailyCheckInService
                .findLatestDailyCheckIn()
                .map(dailyCheckInDtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DailyCheckInResponse> findDailyCheckInById(
            @PathVariable Long id
    ) {
        return dailyCheckInService
                .findDailyCheckInById(id)
                .map(dailyCheckInDtoMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DailyCheckInResponse> createDailyCheckIn(
            @Valid @RequestBody CreateDailyCheckInRequest request
    ) {
        DailyCheckIn dailyCheckIn =
                dailyCheckInDtoMapper.toDomain(request);

        dailyCheckInService.saveDailyCheckIn(dailyCheckIn);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dailyCheckInDtoMapper.toResponse(dailyCheckIn));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailyCheckInResponse> updateDailyCheckIn(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDailyCheckInRequest request
    ) {
        DailyCheckIn dailyCheckIn =
                dailyCheckInDtoMapper.toDomain(id, request);

        boolean updated =
                dailyCheckInService.updateDailyCheckIn(dailyCheckIn);

        if (!updated) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                dailyCheckInDtoMapper.toResponse(dailyCheckIn)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDailyCheckIn(
            @PathVariable Long id
    ) {
        boolean deleted =
                dailyCheckInService.deleteDailyCheckInById(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}