package com.alba.cycleruncoach.controller;

import com.alba.cycleruncoach.domain.DailyCheckIn;
import com.alba.cycleruncoach.service.DailyCheckInService;

import java.util.List;

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

    public DailyCheckInController(
            DailyCheckInService dailyCheckInService
    ) {
        this.dailyCheckInService = dailyCheckInService;
    }

    @GetMapping
    public List<DailyCheckIn> findAllDailyCheckIns() {
        return dailyCheckInService.findAllDailyCheckIns();
    }

    @GetMapping("/latest")
    public ResponseEntity<DailyCheckIn> findLatestDailyCheckIn() {
        return dailyCheckInService
                .findLatestDailyCheckIn()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DailyCheckIn> findDailyCheckInById(
            @PathVariable Long id
    ) {
        return dailyCheckInService
                .findDailyCheckInById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<DailyCheckIn> createDailyCheckIn(
            @RequestBody DailyCheckIn dailyCheckIn
    ) {
        dailyCheckInService.saveDailyCheckIn(dailyCheckIn);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dailyCheckIn);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DailyCheckIn> updateDailyCheckIn(
            @PathVariable Long id,
            @RequestBody DailyCheckIn dailyCheckIn
    ) {
        if (!id.equals(dailyCheckIn.getId())) {
            return ResponseEntity.badRequest().build();
        }

        boolean updated =
                dailyCheckInService.updateDailyCheckIn(dailyCheckIn);

        if (!updated) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dailyCheckIn);
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