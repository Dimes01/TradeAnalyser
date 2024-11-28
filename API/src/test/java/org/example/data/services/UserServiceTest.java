package org.example.data.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.AccountStatus;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    @Qualifier("exchange-user-service")
    private UserService userService;

    @Test
    void getAccounts() throws ExecutionException, InterruptedException {
        var accounts = userService.getAccounts(AccountStatus.ACCOUNT_STATUS_OPEN);
        accounts.forEach(System.out::println);
    }
}