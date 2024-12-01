package org.example.scheduler;

import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.example.auth.models.User;
import org.example.auth.repositories.UserRepository;
import org.example.auth.services.UserService;
import org.example.data.dto.AccountDTO;
import org.example.data.dto.AnalyseRequest;
import org.example.data.dto.AnalyseResponse;
import org.example.data.services.AnalyseService;
import org.example.data.services.ExchangeUserService;
import org.example.data.services.OperationService;
import org.example.data.services.QuotesService;
import org.example.data.utilities.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.tinkoff.piapi.contract.v1.AccountStatus;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@EnableScheduling
@PropertySource("classpath:")
@Slf4j
public class MainScheduler {
    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;
    @Autowired private OperationService operationService;
    @Autowired private QuotesService quotesService;
    @Autowired private AnalyseService analyseService;

    @Value("${ru.tinkoff.piapi.core.api.target}")
    private String target;

    @Value("${services.main-scheduler.max-threads}")
    private int maxThreads;


    public void updateUsers() {
        var users = userRepository.findAll();
        var exchangeUserServices = getExchangeUserServices(users);
        var accounts = getAccounts(exchangeUserServices);
        analyseAccounts(accounts);
    }

    private List<ExchangeUserService> getExchangeUserServices(List<User> users) {
        var exchangeUserServices = new ArrayList<ExchangeUserService>();
        users.forEach(user -> {
            var token = userService.decrypt(user.getToken());
            var interceptor = new AuthInterceptor(token);
            var channel = ManagedChannelBuilder.forTarget(target).useTransportSecurity().intercept(interceptor).build();
            var exchangeUserService = new ExchangeUserService(channel);
            exchangeUserServices.add(exchangeUserService);
            log.info("Make exchange user service {}", exchangeUserService);
        });
        return exchangeUserServices;
    }

    private List<AccountDTO> getAccounts(List<ExchangeUserService> exchangeUserServices) {
        var accountDTOs = new ArrayList<AccountDTO>();
        var futures = new ArrayList<CompletableFuture<Void>>();
        exchangeUserServices.forEach(exchangeUserService -> {
            futures.add(CompletableFuture.runAsync(() -> {
                accountDTOs.addAll(exchangeUserService.getAccounts(AccountStatus.ACCOUNT_STATUS_ALL));
                log.info("Got accounts from exchange user service {}", exchangeUserService);
            }));
        });
        futures.forEach(CompletableFuture::join);
        return accountDTOs;
    }

    private void analyseAccounts(List<AccountDTO> accounts) {
        try (ExecutorService threadPool = Executors.newFixedThreadPool(maxThreads)) {
            for (var account : accounts) {
                // TODO: написать получение безрисковой ставки и бенчмарка от пользователя
                threadPool.submit(() -> analyseAccount(account, 0.08, 0.12));
            }
            threadPool.shutdown();
        }
        log.info("Accounts are analysed");
    }

    private void analyseAccount(AccountDTO account, Double riskFree, Double meanBenchmark) {
        var futures = new ArrayList<CompletableFuture<AnalyseResponse>>();
        var positions = operationService.getPositions(account.getId());

        // Анализируем только ценные бумаги. Фьючерсы и опционы не анализируем
        positions.getSecurities().forEach(securityPosition -> {
            futures.add(CompletableFuture.supplyAsync(() -> {
                var to = Instant.now();
                var from = to.minus(1, ChronoUnit.YEARS);
                var candles = quotesService.getHistoricCandles(securityPosition.getFigi(), to, from, CandleInterval.CANDLE_INTERVAL_DAY);
                return analyseService.analyse(new AnalyseRequest(candles, riskFree, meanBenchmark));
            }));
        });

        // TODO: реализовать сохранение анализов в БД
        log.info("Account {} is analysed", account.getId());
    }


//    @Scheduled(cron = "0 50 9 * * 1-5")
//    public void startTrade() throws ExecutionException, InterruptedException {
//        var result = userServices.getAccounts(AccountStatus.ACCOUNT_STATUS_ALL);
//        System.out.println(result);
//    }
}
