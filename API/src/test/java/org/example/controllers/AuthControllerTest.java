package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.auth.LoginRequest;
import org.example.dto.auth.LoginResponse;
import org.example.dto.auth.RegisterRequest;
import org.example.dto.auth.RegisterResponse;
import org.example.repositories.UserRepository;
import org.example.utilities.CryptUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link AuthController}
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Value("${spring.security.password-encoder.strength}")
    private int strengthEncoder;

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private CryptUtil cryptUtil;
    @Autowired private BCryptPasswordEncoder utilEncoder;
    private final ObjectMapper utilMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(AuthControllerTest.class);

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
        .withInitScript("initdb.sql");

    @BeforeAll
    public static void startContainer() { container.start(); }

    @DynamicPropertySource
    private static void properties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @AfterAll
    public static void stopContainer() { container.stop(); }


    @Test
    public void register_goodSituation_registered() throws Exception {
        // Arrange
        var request = new RegisterRequest("user", "password", "token");
        var requestString = utilMapper.writeValueAsString(request);
        var expectedResponse = new RegisterResponse(request.getUsername(), true, false);
        var expectedToken = cryptUtil.encrypt(request.getApiKey());

        // Act
        var responseString = mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        var response = utilMapper.readValue(responseString, RegisterResponse.class);
        var user = userRepository.findByUsername(request.getUsername());

        // Assert
        assertEquals(expectedResponse, response);
        assertEquals(request.getUsername(), user.getUsername());
        assertEquals(expectedToken, user.getToken());
        assertTrue(utilEncoder.matches(request.getPassword(), user.getPassword()));
    }

    private static Stream<Arguments> badRegisteredRequests() {
        return Stream.of(
            Arguments.of(new RegisterRequest("newUser", "password", "")),
            Arguments.of(new RegisterRequest("newUser", "", "token")),
            Arguments.of(new RegisterRequest("newUser", "", "")),
            Arguments.of(new RegisterRequest("", "password", "token")),
            Arguments.of(new RegisterRequest("", "password", "")),
            Arguments.of(new RegisterRequest("", "", "token")),
            Arguments.of(new RegisterRequest("", "", ""))
        );
    }

    @ParameterizedTest
    @MethodSource("badRegisteredRequests")
    public void register_incorrectRequest_notRegistered(RegisterRequest request) throws Exception {
        // Arrange
        var requestString = utilMapper.writeValueAsString(request);

        // Act
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString))
            .andExpect(status().isBadRequest());

        // Assert
        assertNull(userRepository.findByUsername(request.getUsername()));
    }

    @Test
    public void register_existedUser_notRegistered() throws Exception {
        // Arrange
        var request = new RegisterRequest("username", "password", "token");
        var requestString = utilMapper.writeValueAsString(request);

        // Act
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString))
            .andExpect(status().isBadRequest());

        // Assert
        assertNotNull(userRepository.findByUsername(request.getUsername()));
    }

    @Test
    public void login() throws Exception {
        // Arrange
        var request = new LoginRequest("username", "password");
        var requestString = utilMapper.writeValueAsString(request);

        // Act
        var responseString = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        var response = utilMapper.readValue(responseString, LoginResponse.class);

        // Assert
        assertTrue(response.isSuccess());
    }
}
