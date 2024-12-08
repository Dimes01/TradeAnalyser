package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.repositories.UserRepository;
import org.example.entities.Account;
import org.example.repositories.AccountRepository;
import org.example.services.t_api.UserService_T_API;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.AccountStatus;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public List<Account> getAccountsByUsername(String username) {
        var user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("Username not found!");
            throw new UsernameNotFoundException(username);
        }
        return accountRepository.findAccountsByUserId(user.getId());
    }

    public boolean updateAccountsByApiKey(String decryptedToken) {
        var exchangeUserService = new UserService_T_API(InvestApi.createReadonly(decryptedToken));
        var accounts = exchangeUserService.getAccounts(AccountStatus.ACCOUNT_STATUS_ALL);
        if (accounts.isEmpty()) {
            log.warn("Accounts not found");
            return false;
        }
        accountRepository.saveAll(accounts);
        log.info("Accounts updated");
        return true;
    }
}
