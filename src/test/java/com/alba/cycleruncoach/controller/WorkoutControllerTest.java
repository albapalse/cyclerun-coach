package com.alba.cycleruncoach.controller;

import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.Workout;
import com.alba.cycleruncoach.domain.WorkoutType;
import com.alba.cycleruncoach.service.WorkoutService;
import com.alba.cycleruncoach.exception.DuplicateResourceException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
import static org.mockito.Mockito.doThrow;

import com.alba.cycleruncoach.controller.mapper.WorkoutDtoMapper;
import org.springframework.context.annotation.Import;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@WebMvcTest(WorkoutController.class)
@Import(WorkoutDtoMapper.class)
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
                .thenReturn(Optional.of(workout));

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
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/workouts/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Workout with id 99 was not found"))
                .andExpect(jsonPath("$.path")
                        .value("/api/workouts/99"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());
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
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Workout with id 99 was not found"))
                .andExpect(jsonPath("$.path").value("/api/workouts/99"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());
    }

    @Test
    void updateWorkout_returnsBadRequest_whenDistanceIsNotPositive()
            throws Exception {
        String requestBody = """
            {
              "date": "2026-09-04",
              "distanceKm": 0.0,
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
                .andExpect(jsonPath("$.fieldErrors.distanceKm")
                        .value("Distance must be greater than zero"));

        verify(workoutService, never())
                .updateWorkout(any(Workout.class));
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
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("Workout with id 99 was not found"))
                .andExpect(jsonPath("$.path").value("/api/workouts/99"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidCreateWorkoutRequests")
    void createWorkout_returnsBadRequest_whenRequestIsInvalid(
            String scenario,
            String requestBody
    ) throws Exception {
        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(workoutService, never())
                .saveWorkout(any(Workout.class));
    }

    private static Stream<Arguments> invalidCreateWorkoutRequests() {
        String validRequest = validCreateWorkoutJson();

        return Stream.of(
                Arguments.of(
                        "missing id",
                        validRequest.replace("\"id\": 1,", "")
                ),
                Arguments.of(
                        "future date",
                        validRequest.replace(
                                "\"date\": \"2026-09-04\"",
                                "\"date\": \"2999-01-01\""
                        )
                ),
                Arguments.of(
                        "zero distance",
                        validRequest.replace(
                                "\"distanceKm\": 8.0",
                                "\"distanceKm\": 0.0"
                        )
                ),
                Arguments.of(
                        "zero duration",
                        validRequest.replace(
                                "\"durationMinutes\": 48",
                                "\"durationMinutes\": 0"
                        )
                ),
                Arguments.of(
                        "effort below minimum",
                        validRequest.replace(
                                "\"perceivedEffort\": 5",
                                "\"perceivedEffort\": 0"
                        )
                ),
                Arguments.of(
                        "effort above maximum",
                        validRequest.replace(
                                "\"perceivedEffort\": 5",
                                "\"perceivedEffort\": 11"
                        )
                ),
                Arguments.of(
                        "missing workout type",
                        validRequest.replace(
                                "\"workoutType\": \"EASY_RUN\"",
                                "\"workoutType\": null"
                        )
                ),
                Arguments.of(
                        "unknown cycle phase",
                        validRequest.replace(
                                "\"cyclePhase\": \"FOLLICULAR\"",
                                "\"cyclePhase\": \"UNKNOWN\""
                        )
                )
        );
    }

    private static String validCreateWorkoutJson() {
        return """
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
    }

    @Test
    void createWorkout_returnsConflict_whenIdAlreadyExists()
            throws Exception {
        doThrow(new DuplicateResourceException(
                "Workout with id 1 already exists"
        )).when(workoutService).saveWorkout(any(Workout.class));

        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCreateWorkoutJson()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message")
                        .value("Workout with id 1 already exists"))
                .andExpect(jsonPath("$.path")
                        .value("/api/workouts"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());
    }

    @Test
    void createWorkout_returnsValidationDetails_whenDistanceIsMissing()
            throws Exception {
        String requestBody = validCreateWorkoutJson()
                .replace("\"distanceKm\": 8.0,", "");

        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Request validation failed"))
                .andExpect(jsonPath("$.path")
                        .value("/api/workouts"))
                .andExpect(jsonPath("$.fieldErrors.distanceKm")
                        .value("Distance is required"));

        verify(workoutService, never())
                .saveWorkout(any(Workout.class));
    }
    @Test
    void createWorkout_returnsReadableError_whenEnumIsUnknown()
            throws Exception {
        String requestBody = validCreateWorkoutJson()
                .replace(
                        "\"cyclePhase\": \"FOLLICULAR\"",
                        "\"cyclePhase\": \"UNKNOWN\""
                );

        mockMvc.perform(post("/api/workouts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Malformed or unreadable JSON request"))
                .andExpect(jsonPath("$.path")
                        .value("/api/workouts"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());

        verify(workoutService, never())
                .saveWorkout(any(Workout.class));
    }
    @Test
    void findAllWorkouts_returnsInternalServerError_whenUnexpectedFailureOccurs()
            throws Exception {
        when(workoutService.findAllWorkouts())
                .thenThrow(new RuntimeException(
                        "Internal database connection details"
                ));

        mockMvc.perform(get("/api/workouts"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error")
                        .value("Internal Server Error"))
                .andExpect(jsonPath("$.message")
                        .value("An unexpected error occurred"))
                .andExpect(jsonPath("$.path")
                        .value("/api/workouts"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());
    }


    @Test
    void findWorkoutById_returnsBadRequest_whenIdIsNotPositive()
            throws Exception {
        mockMvc.perform(get("/api/workouts/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Request validation failed"))
                .andExpect(jsonPath("$.path")
                        .value("/api/workouts/0"));
    }
}
