package org.example.data.services;

import lombok.RequiredArgsConstructor;
import org.example.auth.repositories.UserRepository;
import org.example.auth.services.UserService;
import org.example.data.entities.Account;
import org.example.data.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public List<Account> getAccountsIds(String username) {
        var userId = userRepository.findByUsername(username).getId();
        return accountRepository.findAccountsByUserId(userId);
    }

    public boolean updateRiskFree(String accountId, double riskFree) {
        var accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty())
            return false;

        var account = accountOptional.get();
        account.setFiskFree(riskFree);
        accountRepository.save(account);
        return true;
    }

    public boolean updateMeanBenchmark(String accountId, double meanBenchmark) {
        var accountOptional = accountRepository.findById(accountId);
        if (accountOptional.isEmpty())
            return false;

        var account = accountOptional.get();
        account.setMeanBenchmark(meanBenchmark);
        accountRepository.save(account);
        return true;
    }
}
