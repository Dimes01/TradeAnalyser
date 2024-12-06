package org.example.data.services;

import org.example.services.t_api.OperationService_T_API;
import org.example.services.t_api.UserService_T_API;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.AccountStatus;

import java.util.concurrent.ExecutionException;

@SpringBootTest
class OperationServiceTAPITest {
    @Autowired private UserService_T_API userServiceTAPI;
    @Autowired private OperationService_T_API operationServiceTAPI;

    @Test
    void getPositions() throws ExecutionException, InterruptedException {
        var accounts = userServiceTAPI.getAccounts(AccountStatus.ACCOUNT_STATUS_ALL);
        var positions = operationServiceTAPI.getPositions(accounts.getFirst().getId());
        positions.getSecurities().forEach(System.out::println);
    }
}