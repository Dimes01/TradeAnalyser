package org.example.services;

import org.example.TradeAnalyserAuthApplication;
import org.example.entities.Account;
import org.example.entities.User;
import org.example.repositories.AccountRepository;
import org.example.repositories.UserRepository;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = TradeAnalyserAuthApplication.class)
@Testcontainers
class AccountServiceTest {
    @Autowired private AccountService accountService;

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
        .withDatabaseName("testdb")
        .withUsername("username")
        .withPassword("password")
        .withInitScript("initdb.sql");

    @BeforeAll
    public static void startContainer() {
        container.start();
    }

    @DynamicPropertySource
    private static void properties(final DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @AfterAll
    public static void stopContainer() {
        container.stop();
    }

    @Test
    void getAccountsByUsername_correctUsername_correctAccounts() {
        // Arrange
        var correctUser = new User(1, "username", "password", "token");
        var openedDate = Instant.parse("2024-01-10T00:09:00Z");
        var closedDate = Instant.parse("1970-01-01T00:00:00Z");
        var correctAccount = new Account("string_id", correctUser, openedDate, closedDate, 1, "Брокерский счёт", 2, 2);
        var accounts = List.of(correctAccount);

        // Act
        var returnedAccounts = accountService.getAccountsByUsername(correctUser.getUsername());

        // Assert
        assertEquals(accounts.size(), returnedAccounts.size());
        for (int i = 0; i < accounts.size(); ++i) {
            assertEquals(accounts.get(i).getId(), returnedAccounts.get(i).getId());
        }
    }

    @Test
    void getAccountsByUsername_incorrectUsername_throwUsernameNotFound() {
        // Arrange & Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> accountService.getAccountsByUsername("incorrectUsername"));
    }
}