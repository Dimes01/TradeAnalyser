package org.example.data.services;

import io.grpc.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auth.repositories.UserRepository;
import org.example.data.entities.Account;
import org.example.data.repositories.AccountRepository;
import org.example.data.repositories.SettingsRepository;
import org.example.data.utilities.Channels;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.AccountStatus;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final SettingsRepository settingsRepository;
    private final UserRepository userRepository;

    public List<Account> getAccountsByUsername(String username) {
        var userId = userRepository.findByUsername(username).getId();
        return accountRepository.findAccountsByUserId(userId);
    }

    public boolean updateAccountsByUsername(String username, String decryptedToken) {
        Channel channel;
        try {
            channel = Channels.withDecryptedToken(decryptedToken);
        } catch (Exception e) {
            log.error("Failed to decrypt token: {}", e.getMessage());
            return false;
        }
        var exchangeUserService = new ExchangeUserService(channel);
        var accounts = exchangeUserService.getAccounts(AccountStatus.ACCOUNT_STATUS_ALL);
        accountRepository.saveAll(accounts);
        log.info("Accounts updated");
        return true;
    }

    public boolean updateRiskFree(String accountId, double riskFree) {
        var settings = settingsRepository.findByAccountId(accountId);
        if (settings == null) return false;

        settings.setFiskFree(riskFree);
        settingsRepository.save(settings);
        return true;
    }

    public boolean updateMeanBenchmark(String accountId, double meanBenchmark) {
        var settings = settingsRepository.findByAccountId(accountId);
        if (settings == null) return false;

        settings.setMeanBenchmark(meanBenchmark);
        settingsRepository.save(settings);
        return true;
    }
}
