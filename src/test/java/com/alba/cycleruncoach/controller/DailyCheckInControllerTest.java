package com.alba.cycleruncoach.controller;

import com.alba.cycleruncoach.controller.mapper.DailyCheckInDtoMapper;
import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.DailyCheckIn;
import com.alba.cycleruncoach.domain.EnergyLevel;
import com.alba.cycleruncoach.domain.SleepQuality;
import com.alba.cycleruncoach.domain.Symptom;
import com.alba.cycleruncoach.exception.DuplicateResourceException;
import com.alba.cycleruncoach.service.DailyCheckInService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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

@WebMvcTest(DailyCheckInController.class)
@Import(DailyCheckInDtoMapper.class)
class DailyCheckInControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DailyCheckInService dailyCheckInService;

    @Test
    void findAllDailyCheckIns_returnsEmptyList_whenNoCheckInsExist()
            throws Exception {
        when(dailyCheckInService.findAllDailyCheckIns())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/check-ins"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void findDailyCheckInById_returnsCheckIn_whenCheckInExists()
            throws Exception {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(1L);

        when(dailyCheckInService.findDailyCheckInById(1L))
                .thenReturn(Optional.of(dailyCheckIn));

        mockMvc.perform(get("/api/check-ins/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.energyLevel").value("HIGH"))
                .andExpect(jsonPath("$.symptoms[0]").value("FATIGUE"));
    }

    @Test
    void findDailyCheckInById_returnsNotFound_whenCheckInDoesNotExist()
            throws Exception {
        when(dailyCheckInService.findDailyCheckInById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/check-ins/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("We couldn't find a daily check-in with ID 99."))
                .andExpect(jsonPath("$.path")
                        .value("/api/check-ins/99"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());
    }

    @Test
    void findLatestDailyCheckIn_returnsLatestCheckIn_whenOneExists()
            throws Exception {
        DailyCheckIn dailyCheckIn = createDailyCheckIn(1L);

        when(dailyCheckInService.findLatestDailyCheckIn())
                .thenReturn(Optional.of(dailyCheckIn));

        mockMvc.perform(get("/api/check-ins/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.date").value("2026-09-08"));
    }

    @Test
    void findLatestDailyCheckIn_returnsNotFound_whenNoCheckInsExist()
            throws Exception {
        when(dailyCheckInService.findLatestDailyCheckIn())
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/check-ins/latest"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("No daily check-in was found yet."))
                .andExpect(jsonPath("$.path")
                        .value("/api/check-ins/latest"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());
    }

    @Test
    void createDailyCheckIn_returnsCreated_andDelegatesToService()
            throws Exception {
        String requestBody = dailyCheckInJson(1L);

        mockMvc.perform(post("/api/check-ins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sleepHours").value(7.5));

        verify(dailyCheckInService)
                .saveDailyCheckIn(any(DailyCheckIn.class));
    }

    @Test
    void updateDailyCheckIn_returnsUpdatedCheckIn_whenCheckInExists()
            throws Exception {
        when(dailyCheckInService.updateDailyCheckIn(any(DailyCheckIn.class)))
                .thenReturn(true);

        mockMvc.perform(put("/api/check-ins/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDailyCheckInJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.energyLevel").value("HIGH"));
        verify(dailyCheckInService)
                .updateDailyCheckIn(any(DailyCheckIn.class));
    }

    @Test
    void updateDailyCheckIn_returnsNotFound_whenCheckInDoesNotExist()
            throws Exception {
        when(dailyCheckInService.updateDailyCheckIn(any(DailyCheckIn.class)))
                .thenReturn(false);

        mockMvc.perform(put("/api/check-ins/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateDailyCheckInJson()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("We couldn't find a daily check-in with ID 99."))
                .andExpect(jsonPath("$.path")
                        .value("/api/check-ins/99"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());
    }

    @Test
    void updateDailyCheckIn_returnsBadRequest_whenSleepHoursExceedMaximum()
            throws Exception {
        String requestBody = updateDailyCheckInJson()
                .replace(
                        "\"sleepHours\": 7.5",
                        "\"sleepHours\": 24.1"
                );

        mockMvc.perform(put("/api/check-ins/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.sleepHours")
                        .value("Sleep hours must be between 0 and 24."));

        verify(dailyCheckInService, never())
                .updateDailyCheckIn(any(DailyCheckIn.class));
    }

    @Test
    void deleteDailyCheckIn_returnsNoContent_whenCheckInExists()
            throws Exception {
        when(dailyCheckInService.deleteDailyCheckInById(1L))
                .thenReturn(true);

        mockMvc.perform(delete("/api/check-ins/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(dailyCheckInService).deleteDailyCheckInById(1L);
    }

    @Test
    void deleteDailyCheckIn_returnsNotFound_whenCheckInDoesNotExist()
            throws Exception {
        when(dailyCheckInService.deleteDailyCheckInById(99L))
                .thenReturn(false);

        mockMvc.perform(delete("/api/check-ins/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message")
                        .value("We couldn't find a daily check-in with ID 99."))
                .andExpect(jsonPath("$.path")
                        .value("/api/check-ins/99"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());
    }


    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidCreateDailyCheckInRequests")
    void createDailyCheckIn_returnsBadRequest_whenRequestIsInvalid(
            String scenario,
            String requestBody
    ) throws Exception {
        mockMvc.perform(post("/api/check-ins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());

        verify(dailyCheckInService, never())
                .saveDailyCheckIn(any(DailyCheckIn.class));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 24.0})
    void createDailyCheckIn_acceptsBoundarySleepHours(
            double sleepHours
    ) throws Exception {
        String requestBody = dailyCheckInJson(1L)
                .replace(
                        "\"symptoms\": [\"FATIGUE\"]",
                        "\"symptoms\": []"
                )
                .replace(
                        "\"sleepHours\": 7.5",
                        "\"sleepHours\": " + sleepHours
                );

        mockMvc.perform(post("/api/check-ins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sleepHours").value(sleepHours))
                .andExpect(jsonPath("$.symptoms").isEmpty());

        verify(dailyCheckInService)
                .saveDailyCheckIn(any(DailyCheckIn.class));
    }

    @Test
    void createDailyCheckIn_returnsConflict_whenIdAlreadyExists()
            throws Exception {
        doThrow(new DuplicateResourceException(
                "A daily check-in with ID 1 already exists. Please use a different ID."
        )).when(dailyCheckInService)
                .saveDailyCheckIn(any(DailyCheckIn.class));

        mockMvc.perform(post("/api/check-ins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dailyCheckInJson(1L)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message")
                        .value("A daily check-in with ID 1 already exists. Please use a different ID."))
                .andExpect(jsonPath("$.path")
                        .value("/api/check-ins"))
                .andExpect(jsonPath("$.fieldErrors").isEmpty());
    }

    @Test
    void findDailyCheckInById_returnsBadRequest_whenIdIsNotPositive()
            throws Exception {
        mockMvc.perform(get("/api/check-ins/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Some request values are invalid. Please review the details below."))
                .andExpect(jsonPath("$.path")
                        .value("/api/check-ins/0"));
    }
    private static Stream<Arguments> invalidCreateDailyCheckInRequests() {
        String validRequest = dailyCheckInJson(1L);

        return Stream.of(
                Arguments.of(
                        "missing id",
                        validRequest.replace("\"id\": 1,", "")
                ),
                Arguments.of(
                        "future date",
                        validRequest.replace(
                                "\"date\": \"2026-09-08\"",
                                "\"date\": \"2999-01-01\""
                        )
                ),
                Arguments.of(
                        "missing cycle phase",
                        validRequest.replace(
                                "\"cyclePhase\": \"FOLLICULAR\"",
                                "\"cyclePhase\": null"
                        )
                ),
                Arguments.of(
                        "unknown energy level",
                        validRequest.replace(
                                "\"energyLevel\": \"HIGH\"",
                                "\"energyLevel\": \"UNKNOWN\""
                        )
                ),
                Arguments.of(
                        "missing sleep quality",
                        validRequest.replace(
                                "\"sleepQuality\": \"GOOD\"",
                                "\"sleepQuality\": null"
                        )
                ),
                Arguments.of(
                        "missing symptoms",
                        validRequest.replace(
                                "\"symptoms\": [\"FATIGUE\"]",
                                "\"symptoms\": null"
                        )
                ),
                Arguments.of(
                        "null symptom",
                        validRequest.replace(
                                "\"symptoms\": [\"FATIGUE\"]",
                                "\"symptoms\": [null]"
                        )
                ),
                Arguments.of(
                        "sleep hours below minimum",
                        validRequest.replace(
                                "\"sleepHours\": 7.5",
                                "\"sleepHours\": -0.1"
                        )
                ),
                Arguments.of(
                        "sleep hours above maximum",
                        validRequest.replace(
                                "\"sleepHours\": 7.5",
                                "\"sleepHours\": 24.1"
                        )
                )
        );
    }
    private DailyCheckIn createDailyCheckIn(Long id) {
        return new DailyCheckIn(
                id,
                LocalDate.of(2026, 9, 8),
                CyclePhase.FOLLICULAR,
                EnergyLevel.HIGH,
                SleepQuality.GOOD,
                Set.of(Symptom.FATIGUE),
                7.5
        );
    }

    private static String dailyCheckInJson(Long id) {
        return """
                {
                  "id": %d,
                  "date": "2026-09-08",
                  "cyclePhase": "FOLLICULAR",
                  "energyLevel": "HIGH",
                  "sleepQuality": "GOOD",
                  "symptoms": ["FATIGUE"],
                  "sleepHours": 7.5
                }
                """.formatted(id);
    }
    private String updateDailyCheckInJson() {
        return """
            {
              "date": "2026-09-08",
              "cyclePhase": "FOLLICULAR",
              "energyLevel": "HIGH",
              "sleepQuality": "GOOD",
              "symptoms": ["FATIGUE"],
              "sleepHours": 7.5
            }
            """;
    }
}
