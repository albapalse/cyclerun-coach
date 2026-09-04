package com.alba.cycleruncoach.controller;

import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.Workout;
import com.alba.cycleruncoach.domain.WorkoutType;
import com.alba.cycleruncoach.service.WorkoutService;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkoutController.class)
class WorkoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WorkoutService workoutService;

    @Test
    void findAllWorkouts_returnsEmptyList_whenNoWorkoutsExist()
            throws Exception {
        when(workoutService.findAllWorkouts())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/workouts"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void findWorkoutById_returnsWorkout_whenWorkoutExists()
            throws Exception {
        Workout workout = new Workout(
                1L,
                LocalDate.of(2026, 9, 4),
                8.0,
                48,
                5,
                WorkoutType.EASY_RUN,
                CyclePhase.FOLLICULAR
        );

        when(workoutService.findWorkoutById(1L))
                .thenReturn(workout);

        mockMvc.perform(get("/api/workouts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.distanceKm").value(8.0))
                .andExpect(jsonPath("$.workoutType").value("EASY_RUN"));
    }

    @Test
    void findWorkoutById_returnsNotFound_whenWorkoutDoesNotExist()
            throws Exception {
        when(workoutService.findWorkoutById(99L))
                .thenReturn(null);

        mockMvc.perform(get("/api/workouts/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void createWorkout_returnsCreated_andDelegatesToService()
            throws Exception {
        String requestBody = """
                {
                  "id": 1,
                  "date": "2026-09-04",
                  "distanceKm": 8.0,
                  "durationMinutes": 48,
                  "perceivedEffort": 5,
                  "workoutType": "EASY_RUN",
                  "cyclePhase": "FOLLICULAR"
                }
                """;

        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.distanceKm").value(8.0));

        verify(workoutService).saveWorkout(any(Workout.class));
    }

    @Test
    void updateWorkout_returnsUpdatedWorkout_whenWorkoutExists()
            throws Exception {
        String requestBody = """
                {
                  "id": 1,
                  "date": "2026-09-04",
                  "distanceKm": 10.0,
                  "durationMinutes": 55,
                  "perceivedEffort": 8,
                  "workoutType": "TEMPO_RUN",
                  "cyclePhase": "LUTEAL"
                }
                """;
        when(workoutService.updateWorkout(any(Workout.class)))
                .thenReturn(true);

        mockMvc.perform(put("/api/workouts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.distanceKm").value(10.0))
                .andExpect(jsonPath("$.workoutType").value("TEMPO_RUN"));

        verify(workoutService).updateWorkout(any(Workout.class));
    }

    @Test
    void updateWorkout_returnsNotFound_whenWorkoutDoesNotExist()
            throws Exception {
        String requestBody = """
                {
                  "id": 99,
                  "date": "2026-09-04",
                  "distanceKm": 5.0,
                  "durationMinutes": 30,
                  "perceivedEffort": 4,
                  "workoutType": "RECOVERY_RUN",
                  "cyclePhase": "LUTEAL"
                }
                """;
        when(workoutService.updateWorkout(any(Workout.class)))
                .thenReturn(false);

        mockMvc.perform(put("/api/workouts/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void updateWorkout_returnsBadRequest_whenPathIdAndBodyIdDiffer()
            throws Exception {
        String requestBody = """
                {
                  "id": 2,
                  "date": "2026-09-04",
                  "distanceKm": 5.0,
                  "durationMinutes": 30,
                  "perceivedEffort": 4,
                  "workoutType": "RECOVERY_RUN",
                  "cyclePhase": "LUTEAL"
                }
                """;

        mockMvc.perform(put("/api/workouts/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));

        verify(workoutService, never()).updateWorkout(any(Workout.class));
    }

    @Test
    void deleteWorkout_returnsNoContent_whenWorkoutExists()
            throws Exception {
        when(workoutService.deleteWorkoutById(1L))
                .thenReturn(true);

        mockMvc.perform(delete("/api/workouts/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(workoutService).deleteWorkoutById(1L);
    }

    @Test
    void deleteWorkout_returnsNotFound_whenWorkoutDoesNotExist()
            throws Exception {
        when(workoutService.deleteWorkoutById(99L))
                .thenReturn(false);

        mockMvc.perform(delete("/api/workouts/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }
}
