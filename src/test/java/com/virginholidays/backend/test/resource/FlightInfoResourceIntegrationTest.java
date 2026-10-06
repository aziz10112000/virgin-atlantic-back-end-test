package com.virginholidays.backend.test.resource;

import com.virginholidays.backend.test.BackEndTestApplication;
import com.virginholidays.backend.test.api.ErrorResponse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(classes = BackEndTestApplication.class)
@AutoConfigureMockMvc
class FlightInfoResourceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnFlightsForRequestedDate() throws Exception {

        MvcResult result = mockMvc.perform(get("/2026-10-05/results"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(status().isOk());
    }
    

    @Test
    void shouldReturnStructuredErrorForInvalidDate() throws Exception {

        mockMvc.perform(get("/2026-02-30/results"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message")
                        .value("Invalid date. Expected format yyyy-MM-dd."))
                .andExpect(jsonPath("$.path")
                        .value("/2026-02-30/results"));
    }
}

