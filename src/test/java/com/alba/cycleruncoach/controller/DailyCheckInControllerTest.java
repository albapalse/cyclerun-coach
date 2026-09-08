package com.alba.cycleruncoach.controller;

import com.alba.cycleruncoach.domain.CyclePhase;
import com.alba.cycleruncoach.domain.DailyCheckIn;
import com.alba.cycleruncoach.domain.EnergyLevel;
import com.alba.cycleruncoach.domain.SleepQuality;
import com.alba.cycleruncoach.domain.Symptom;
import com.alba.cycleruncoach.service.DailyCheckInService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

@WebMvcTest(DailyCheckInController.class)
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
                .andExpect(content().string(""));
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
                .andExpect(content().string(""));
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
                        .content(dailyCheckInJson(1L)))
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
                        .content(dailyCheckInJson(99L)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    void updateDailyCheckIn_returnsBadRequest_whenPathIdAndBodyIdDiffer()
            throws Exception {
        mockMvc.perform(put("/api/check-ins/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(dailyCheckInJson(3L)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));

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
                .andExpect(content().string(""));
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

    private String dailyCheckInJson(Long id) {
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
}
