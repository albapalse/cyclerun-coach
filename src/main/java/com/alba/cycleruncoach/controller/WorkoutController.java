package com.alba.cycleruncoach.controller;

import com.alba.cycleruncoach.domain.Workout;
import com.alba.cycleruncoach.service.WorkoutService;
import com.alba.cycleruncoach.exception.ResourceNotFoundException;

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
import com.alba.cycleruncoach.controller.dto.CreateWorkoutRequest;
import com.alba.cycleruncoach.controller.dto.WorkoutResponse;
import com.alba.cycleruncoach.controller.mapper.WorkoutDtoMapper;
import com.alba.cycleruncoach.controller.dto.UpdateWorkoutRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;
    private final WorkoutDtoMapper workoutDtoMapper;

    public WorkoutController(
            WorkoutService workoutService,
            WorkoutDtoMapper workoutDtoMapper
    ) {
        this.workoutService = workoutService;
        this.workoutDtoMapper = workoutDtoMapper;
    }

    @GetMapping
    public List<WorkoutResponse> findAllWorkouts() {
        return workoutService
                .findAllWorkouts()
                .stream()
                .map(workoutDtoMapper::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutResponse> findWorkoutById(
            @PathVariable @Positive(message = "Please use an ID greater than zero.") Long id
    ) {
        Workout workout = workoutService
                .findWorkoutById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "We couldn't find a workout with ID " + id + "."
                ));

        return ResponseEntity.ok(
                workoutDtoMapper.toResponse(workout)
        );
    }

    @PostMapping
    public ResponseEntity<WorkoutResponse> createWorkout(
            @Valid @RequestBody CreateWorkoutRequest request
    ) {
        Workout workout = workoutDtoMapper.toDomain(request);

        workoutService.saveWorkout(workout);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(workoutDtoMapper.toResponse(workout));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutResponse> updateWorkout(
            @PathVariable @Positive(message = "Please use an ID greater than zero.") Long id,
            @Valid @RequestBody UpdateWorkoutRequest request
    ) {
        Workout workout = workoutDtoMapper.toDomain(id, request);

        boolean updated = workoutService.updateWorkout(workout);

        if (!updated) {
            throw new ResourceNotFoundException(
                    "We couldn't find a workout with ID " + id + "."
            );
        }

        return ResponseEntity.ok(
                workoutDtoMapper.toResponse(workout)
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkout(
            @PathVariable @Positive(message = "Please use an ID greater than zero.") Long id
    ) {
        boolean deleted = workoutService.deleteWorkoutById(id);

        if (!deleted) {
            throw new ResourceNotFoundException(
                    "We couldn't find a workout with ID " + id + "."
            );
        }

        return ResponseEntity.noContent().build();
    }
}
