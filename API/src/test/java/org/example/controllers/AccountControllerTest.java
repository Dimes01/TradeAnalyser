package org.example.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.example.entities.Account;
import org.example.entities.User;
import org.example.repositories.LatestAnalyseRepository;
import org.example.repositories.UserRepository;
import org.example.services.AccountService;
import org.example.services.SettingsService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test class for the {@link AccountController}
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {
    private AutoCloseable closeable;
    @Mock private AccountService accountService;
    @Mock private SettingsService settingsService;
    @Mock private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AccountController accountController;

    private final Instant openedDate = Instant.parse("2024-01-09T09:30:00Z");
    private final Instant closedDate = Instant.parse("2024-01-09T18:30:00Z");
    private final User correctUser = new User(1, "username", "password", "token");
    private final Account correctAccount = new Account("string_id", correctUser, openedDate, closedDate, 1, "Брокерский счёт", 2, 2);
    private final List<Account> accounts = Collections.singletonList(correctAccount);
    private final ObjectMapper utilMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

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
    public void getAccountsByUsername_correctUsername_returnAccounts() throws Exception {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(correctUser);
        when(accountService.getAccountsByUsername(correctUser.getUsername())).thenReturn(accounts);

        // Act & Assert
        String returnedAccountsString = mockMvc.perform(get("/api/account/{username}/accounts", correctUser.getUsername())
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(correctUser.getUsername())))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        var returnedAccounts = utilMapper.readValue(returnedAccountsString, new TypeReference<List<String>>() {});
        assertEquals(accounts.size(), returnedAccounts.size());
        for (int i = 0; i < accounts.size(); ++i) {
            assertEquals(accounts.get(i).getId(), returnedAccounts.get(i));
        }
    }

    @Test
    public void getAccountsByUsername_incorrectUsername_returnUnauthorized() throws Exception {
        // Arrange
        when(userRepository.findByUsername(anyString())).thenReturn(correctUser);
        when(accountService.getAccountsByUsername(anyString())).thenThrow(UsernameNotFoundException.class);

        // Act & Assert
        String returnedAccountsString = mockMvc.perform(get("/api/account/{username}/accounts", "incorrectUsername")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
            .andExpect(status().isUnauthorized())
            .andReturn().getResponse().getContentAsString();
        assertThrows(MismatchedInputException.class, () -> utilMapper.readValue(returnedAccountsString, new TypeReference<List<String>>() {}));
    }

    @Disabled
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

    @Disabled
    @Test
    public void setRiskFree() throws Exception {
        String username = "";

        mockMvc.perform(put("/api/account/{2}/{0}/risk-free/{1}", "", "0", "")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user("user")))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Disabled
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
