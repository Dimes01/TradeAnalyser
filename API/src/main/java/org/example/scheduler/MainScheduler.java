package org.example.scheduler;

import org.example.data.services.ExchangeUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.AccountStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
@EnableScheduling
@PropertySource("classpath:")
public class MainScheduler {

    @Value("${ru.tinkoff.piapi.core.api.target}")
    private String target;

    private final List<ExchangeUserService> userServices = new ArrayList<>();

    public void updateUsers() {

        userServices.clear();
    }


//    @Scheduled(cron = "0 50 9 * * 1-5")
//    public void startTrade() throws ExecutionException, InterruptedException {
//        var result = userServices.getAccounts(AccountStatus.ACCOUNT_STATUS_ALL);
//        System.out.println(result);
//    }
}
