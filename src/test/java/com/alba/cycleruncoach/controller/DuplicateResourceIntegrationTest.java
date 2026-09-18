package com.alba.cycleruncoach.controller;

import com.alba.cycleruncoach.repository.jpa.SpringDataDailyCheckInJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DuplicateResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SpringDataDailyCheckInJpaRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @AfterEach
    void cleanUp() {
        repository.deleteAll();
    }

    @Test
    void createDailyCheckIn_returnsConflict_whenIdAlreadyExistsInDatabase()
            throws Exception {
        String requestBody = """
                {
                  "id": 1001,
                  "date": "2026-09-16",
                  "cyclePhase": "FOLLICULAR",
                  "energyLevel": "HIGH",
                  "sleepQuality": "GOOD",
                  "symptoms": [],
                  "sleepHours": 7.5
                }
                """;

        mockMvc.perform(post("/api/check-ins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/check-ins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value(
                        "A daily check-in with ID 1001 already exists. Please use a different ID."
                ))
                .andExpect(jsonPath("$.path").value("/api/check-ins"));
    }
}
