package org.example.services;

import io.grpc.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.repositories.UserRepository;
import org.example.entities.Account;
import org.example.repositories.AccountRepository;
import org.example.services.t_api.UserService_T_API;
import org.example.utilities.Channels;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.AccountStatus;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final Channels channels;

    public List<Account> getAccountsByUsername(String username) {
        var userId = userRepository.findByUsername(username).getId();
        return accountRepository.findAccountsByUserId(userId);
    }

    public boolean updateAccountsByApiKey(String decryptedToken) {
        Channel channel;
        try {
            channel = channels.withDecryptedToken(decryptedToken);
        } catch (Exception e) {
            log.error("Failed to decrypt token: {}", e.getMessage());
            return false;
        }
        var exchangeUserService = new UserService_T_API(channel);
        var accounts = exchangeUserService.getAccounts(AccountStatus.ACCOUNT_STATUS_ALL);
        accountRepository.saveAll(accounts);
        log.info("Accounts updated");
        return true;
    }
}
