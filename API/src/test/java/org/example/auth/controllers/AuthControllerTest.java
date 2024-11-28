package org.example.auth.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.auth.controllers.AuthController;
import org.example.auth.dto.JwtRequest;
import org.example.auth.dto.JwtResponse;
import org.example.auth.dto.RefreshJwtRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link AuthController}
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    private final JwtRequest jwtRequest = new JwtRequest("user", "1234");
    private final ObjectMapper utilMapper = new ObjectMapper();

    @Autowired
    @InjectMocks
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {

    }

    private JwtResponse getTokens(JwtRequest request) throws Exception {
        String authRequest = utilMapper.writeValueAsString(request);

        String responseString = mockMvc.perform(post("/api/auth/login")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user"))
                .content(authRequest)
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();

        return utilMapper.readValue(responseString, JwtResponse.class);
    }

    @Test
    public void login() throws Exception {
        // Arrange
        String authRequest = utilMapper.writeValueAsString(jwtRequest);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user"))
                .content(authRequest)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void getNewAccessToken() throws Exception {
        // Arrange
        JwtResponse tokensBefore = getTokens(jwtRequest);
        RefreshJwtRequest request = new RefreshJwtRequest(tokensBefore.getRefreshToken());
        String requestString = utilMapper.writeValueAsString(request);

        // Act
        String responseString = mockMvc.perform(post("/api/auth/token")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user"))
                .content(requestString)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        JwtResponse tokensAfter = utilMapper.readValue(responseString, JwtResponse.class);

        // Assert
        assertEquals(tokensBefore.getRefreshToken(), tokensAfter.getRefreshToken());
        assertNotEquals(tokensBefore.getAccessToken(), tokensAfter.getAccessToken());
    }


    @Test
    public void getNewRefreshToken() throws Exception {
        // Arrange
        JwtResponse tokensBefore = getTokens(jwtRequest);
        RefreshJwtRequest request = new RefreshJwtRequest(tokensBefore.getRefreshToken());
        String requestString = utilMapper.writeValueAsString(request);

        // Act
        String responseString = mockMvc.perform(post("/api/auth/refresh")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user"))
                .content(requestString)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        JwtResponse tokensAfter = utilMapper.readValue(responseString, JwtResponse.class);

        // Assert
        assertNotEquals(tokensBefore.getRefreshToken(), tokensAfter.getRefreshToken());
        assertNotEquals(tokensBefore.getAccessToken(), tokensAfter.getAccessToken());
    }
}
