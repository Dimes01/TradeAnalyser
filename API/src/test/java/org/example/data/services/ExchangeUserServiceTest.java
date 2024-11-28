package org.example.data.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.AccountStatus;

import java.util.concurrent.ExecutionException;

@SpringBootTest
class ExchangeUserServiceTest {
    @Autowired private ExchangeUserService exchangeUserService;

    @Test
    void getAccounts() throws ExecutionException, InterruptedException {
        var accounts = exchangeUserService.getAccounts(AccountStatus.ACCOUNT_STATUS_OPEN);
        accounts.forEach(System.out::println);
    }
}