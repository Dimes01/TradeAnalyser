package org.example.controllers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.example.dto.AnalyseRequest;
import org.example.dto.AnalyseResponse;
import org.example.entities.Account;
import org.example.entities.LatestAnalyse;
import org.example.repositories.LatestAnalyseRepository;
import org.example.services.AccountService;
import org.example.services.AnalyseService;
import org.example.services.SettingsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// username в url добавлен только для авторизации

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {
    private final LatestAnalyseRepository latestAnalyseRepository;

    private final AccountService accountService;
    private final SettingsService settingsService;


    @GetMapping("/{username}/accounts")
    public ResponseEntity<List<String>> getAccountsByUsername(
        @PathVariable @NotBlank String username
    ) {
        var accounts = accountService.getAccountsByUsername(username);
        var accountIds = accounts.stream().map(Account::getId).toList();
        return ResponseEntity.ok(accountIds);
    }

    @GetMapping("/{username}/{accountId}/analyse/securities")
    public ResponseEntity<List<LatestAnalyse>> analyse(
        @PathVariable @NotBlank String accountId
    ) {
        return ResponseEntity.ok(latestAnalyseRepository.findByAccountId(accountId));
    }

    @GetMapping("/{username}/{accountId}/analyse/securities/{securitiesId}")
    public ResponseEntity<LatestAnalyse> analyse(
        @PathVariable @NotBlank String accountId,
        @PathVariable @NotBlank String securitiesId
    ) {
        return ResponseEntity.ok(latestAnalyseRepository.findByAccountIdAndSecuritiesUid(accountId, securitiesId));
    }

    @PutMapping("/{username}/{accountId}/risk-free/{newValue}")
    public ResponseEntity<Void> setRiskFree(
        @PathVariable @NotBlank(message = "Account ID is required")  String accountId,
        @PathVariable @Positive(message = "New risk free should be positive") double newValue
    ) {
        if (settingsService.updateRiskFree(accountId, newValue))
            return ResponseEntity.ok().build();
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{username}/{accountId}/mean-benchmark/{newValue}")
    public ResponseEntity<Void> setMeanBenchmark(
        @PathVariable @NotBlank(message = "Account ID is required")  String accountId,
        @PathVariable @Positive(message = "New mean of benchmark should be positive") double newValue
    ) {
        if (settingsService.updateMeanBenchmark(accountId, newValue))
            return ResponseEntity.ok().build();
        return ResponseEntity.noContent().build();
    }
}
