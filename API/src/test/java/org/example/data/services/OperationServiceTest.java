package org.example.data.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.AccountStatus;

import java.util.concurrent.ExecutionException;

@SpringBootTest
class OperationServiceTest {
    @Autowired private ExchangeUserService exchangeUserService;
    @Autowired private OperationService operationService;

    @Test
    void getPositions() throws ExecutionException, InterruptedException {
        var accounts = exchangeUserService.getAccounts(AccountStatus.ACCOUNT_STATUS_ALL);
        var positions = operationService.getPositions(accounts.getFirst().getId());
        positions.getSecurities().forEach(System.out::println);
    }
}