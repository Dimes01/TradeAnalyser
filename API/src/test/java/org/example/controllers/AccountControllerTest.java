package org.example.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link AccountController}
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {

    }

    @Test
    public void getAccountsByUsername() throws Exception {
        mockMvc.perform(get("/api/account/{0}/accounts", "")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user")))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    public void analyse() throws Exception {
        String analyseRequest = """
            {
                "securities_id": "",
                "candles": [],
                "riskFree": 0,
                "meanBenchmark": 0
            }""";
        String username = "";

        mockMvc.perform(post("/api/account/{1}/{0}/analyse/securities", "", "")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user"))
                .content(analyseRequest)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    public void setRiskFree() throws Exception {
        String username = "";

        mockMvc.perform(put("/api/account/{2}/{0}/risk-free/{1}", "", "0", "")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user")))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    public void setMeanBenchmark() throws Exception {
        String username = "";

        mockMvc.perform(put("/api/account/{2}/{0}/mean-benchmark/{1}", "", "0", "")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user")))
            .andExpect(status().isOk())
            .andDo(print());
    }
}
