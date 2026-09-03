package com.alba.cycleruncoach.controller;

import com.alba.cycleruncoach.domain.Workout;
import com.alba.cycleruncoach.service.WorkoutService;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping
    public List<Workout> findAllWorkouts() {
        return workoutService.findAllWorkouts();
    }
}