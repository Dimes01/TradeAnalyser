package org.example.data.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.data.entities.Account;
import org.example.data.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{username}")
    public ResponseEntity<List<String>> getAccountIds(
        @PathVariable @NotBlank String username
    ) {
        var accounts = accountService.getAccountsIds(username);
        var accountIds = accounts.stream().map(Account::getId).toList();
        return ResponseEntity.ok(accountIds);
    }

    @PutMapping("/{accountId}/risk-free/{newValue}")
    public ResponseEntity<Boolean> setRiskFree(
        @PathVariable @NotBlank(message = "Account ID is required")  String accountId,
        @PathVariable @Positive(message = "New risk free should be positive") double newValue
    ) {
        if (accountService.updateRiskFree(accountId, newValue))
            return ResponseEntity.ok(true);
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/{accountId}/mean-benchmark/{newValue}")
    public ResponseEntity<Boolean> setMeanBenchmark(
        @PathVariable @NotBlank(message = "Account ID is required")  String accountId,
        @PathVariable @Positive(message = "New mean of benchmark should be positive") double newValue
    ) {
        if (accountService.updateMeanBenchmark(accountId, newValue))
            return ResponseEntity.ok(true);
        return ResponseEntity.badRequest().build();
    }
}
