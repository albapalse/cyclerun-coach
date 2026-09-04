package com.alba.cycleruncoach.controller;

import com.alba.cycleruncoach.domain.Workout;
import com.alba.cycleruncoach.service.WorkoutService;

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

    @GetMapping("/{id}")
    public ResponseEntity<Workout> findWorkoutById(
            @PathVariable Long id
    ) {
        Workout workout = workoutService.findWorkoutById(id);

        if (workout == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(workout);
    }

    @PostMapping
    public ResponseEntity<Workout> createWorkout(
            @RequestBody Workout workout
    ) {
        workoutService.saveWorkout(workout);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(workout);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Workout> updateWorkout(
            @PathVariable Long id,
            @RequestBody Workout workout
    ) {
        if (!id.equals(workout.getId())) {
            return ResponseEntity.badRequest().build();
        }

        boolean updated = workoutService.updateWorkout(workout);

        if (!updated) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(workout);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(
            @PathVariable Long id
    ) {
        boolean deleted = workoutService.deleteWorkoutById(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }
}
