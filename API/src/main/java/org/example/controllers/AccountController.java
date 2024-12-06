package org.example.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.entities.Account;
import org.example.services.AccountService;
import org.example.services.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final SettingsService settingsService;

    @GetMapping("/{username}")
    public ResponseEntity<List<String>> getAccountsByUsername(
        @PathVariable @NotBlank String username
    ) {
        var accounts = accountService.getAccountsByUsername(username);
        var accountIds = accounts.stream().map(Account::getId).toList();
        return ResponseEntity.ok(accountIds);
    }

    // TODO: реализовать авторизацию через указание requestMatchers.access в SecurityConfiguration
    @PutMapping("/{accountId}/risk-free/{newValue}")
    public ResponseEntity<Void> setRiskFree(
        @PathVariable @NotBlank(message = "Account ID is required")  String accountId,
        @PathVariable @Positive(message = "New risk free should be positive") double newValue
    ) {
        if (settingsService.updateRiskFree(accountId, newValue))
            return ResponseEntity.ok().build();
        return ResponseEntity.noContent().build();
    }

    // TODO: реализовать авторизацию через указание requestMatchers.access в SecurityConfiguration
    @PutMapping("/{accountId}/mean-benchmark/{newValue}")
    public ResponseEntity<Void> setMeanBenchmark(
        @PathVariable @NotBlank(message = "Account ID is required")  String accountId,
        @PathVariable @Positive(message = "New mean of benchmark should be positive") double newValue
    ) {
        if (settingsService.updateMeanBenchmark(accountId, newValue))
            return ResponseEntity.ok().build();
        return ResponseEntity.noContent().build();
    }
}
