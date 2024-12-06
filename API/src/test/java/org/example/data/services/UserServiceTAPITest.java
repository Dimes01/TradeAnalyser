package org.example.data.services;

import org.example.services.t_api.UserService_T_API;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.tinkoff.piapi.contract.v1.AccountStatus;

import java.util.concurrent.ExecutionException;

@SpringBootTest
class UserServiceTAPITest {
    @Autowired private UserService_T_API userServiceTAPI;

    @Test
    void getAccounts() throws ExecutionException, InterruptedException {
        var accounts = userServiceTAPI.getAccounts(AccountStatus.ACCOUNT_STATUS_OPEN);
        accounts.forEach(System.out::println);
    }
}