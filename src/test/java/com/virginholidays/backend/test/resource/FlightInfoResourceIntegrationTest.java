package com.virginholidays.backend.test.resource;

import com.virginholidays.backend.test.BackEndTestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
}

