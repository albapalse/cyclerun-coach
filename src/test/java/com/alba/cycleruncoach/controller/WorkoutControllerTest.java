package com.alba.cycleruncoach.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WorkoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void findAllWorkouts_returnsEmptyList_whenNoWorkoutsExist() throws Exception {
        mockMvc.perform(get("/api/workouts"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}