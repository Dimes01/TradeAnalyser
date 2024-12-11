package org.example.services;

import org.example.entities.Account;
import org.example.entities.Settings;
import org.example.entities.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class SettingsServiceTest {
    @Autowired private SettingsService settingsService;

    private final User correctUser = new User(1, "username", "password", "token");
    private final Instant openedDate = Instant.parse("2024-01-10T00:09:00Z");
    private final Instant closedDate = Instant.parse("1970-01-01T00:00:00Z");
    private final Account correctAccount = new Account("string_id", correctUser, openedDate, closedDate, 1, "Брокерский счёт", 2, 2);
    private final Settings correctSettings = new Settings(1, correctAccount, 0.1, 0.15);

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
    void findAll() {
        // Arrange
        var expectedListSettings = List.of(correctSettings);

        // Act
        var returnedListSettings = settingsService.findAll();

        // Assert
        assertEquals(expectedListSettings.size(), returnedListSettings.size());
        for (int i = 0; i < returnedListSettings.size(); ++i) {
            assertEquals(expectedListSettings.get(i).getId(), returnedListSettings.get(i).getId());
        }
    }

    @Test
    void findByAccountId_correctId_correctSettings() {
        var returnedSettings = settingsService.findByAccountId(correctAccount.getId());
        assertEquals(correctSettings.getId(), returnedSettings.getId());
    }

    @Test
    void findByAccountId_incorrectId_returnNull() {
        var returnedSettings = settingsService.findByAccountId("incorrectId");
        assertNull(returnedSettings);
    }

    @Test
    void updateRiskFree_correctId_correctChange() {
        // Arrange
        var expectedRiskFree = 0.2;

        // Act
        var result = settingsService.updateRiskFree(correctAccount.getId(), expectedRiskFree);
        var returnedRiskFree = settingsService.findByAccountId(correctAccount.getId()).getRiskFree();
        settingsService.updateRiskFree(correctAccount.getId(), correctSettings.getRiskFree());

        // Assert
        assertTrue(result);
        assertEquals(expectedRiskFree, returnedRiskFree);
    }

    @Test
    void updateRiskFree_incorrectId_withoutChange() {
        // Arrange
        var newRiskFree = correctSettings.getRiskFree() + 0.1;

        // Act
        var result = settingsService.updateRiskFree("incorrectId", newRiskFree);

        // Assert
        assertFalse(result);
        assertNotEquals(correctSettings.getRiskFree(), newRiskFree);
    }

    @Test
    void updateMeanBenchmark() {
        // Arrange
        var expectedMeanBenchmark = 0.2;

        // Act
        var result = settingsService.updateMeanBenchmark(correctAccount.getId(), expectedMeanBenchmark);
        var returnedMeanBenchmark = settingsService.findByAccountId(correctAccount.getId()).getMeanBenchmark();
        settingsService.updateMeanBenchmark(correctAccount.getId(), correctSettings.getMeanBenchmark());

        // Assert
        assertTrue(result);
        assertEquals(expectedMeanBenchmark, returnedMeanBenchmark);
    }

    @Test
    void updateMeanBenchmark_incorrectId_withoutChange() {
        // Arrange
        var newMeanBenchmark = correctSettings.getMeanBenchmark() + 0.1;

        // Act
        var result = settingsService.updateMeanBenchmark("incorrectId", newMeanBenchmark);

        // Assert
        assertFalse(result);
        assertNotEquals(correctSettings.getMeanBenchmark(), newMeanBenchmark);
    }
}