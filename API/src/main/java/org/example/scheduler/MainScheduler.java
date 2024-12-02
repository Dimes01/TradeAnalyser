package org.example.scheduler;

import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auth.models.User;
import org.example.auth.repositories.UserRepository;
import org.example.auth.services.UserService;
import org.example.data.dto.AnalyseRequest;
import org.example.data.entities.Account;
import org.example.data.repositories.AccountRepository;
import org.example.data.repositories.AnalyseRepository;
import org.example.data.services.AnalyseService;
import org.example.data.services.ExchangeUserService;
import org.example.data.services.OperationService;
import org.example.data.services.QuotesService;
import org.example.data.utilities.AuthInterceptor;
import org.example.data.utilities.MapperEntities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.AccountStatus;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <u>MainScheduler</u> выполняет следующие задачи:
 * <ul>
 *     <li>анализ портфелей по расписанию</li>
 *     <li>сохранение результатов анализа в базу данных</li>
 * </ul>
 *
 * Для анализа портфелей необходимо:
 * <ol>
 *     <li>Получить для каждого пользователя список счётов</li>
 *     <li>По каждому счету определить список инструментов для анализа</li>
 *     <li>Для каждого инструмента достать из <b>API</b> исторические свечи</li>
 *     <li>Полученные свечи проанализировать</li>
 *     <li>Анализ свечей сохранить в БД</li>
 * </ol>
 */



@Component
@EnableScheduling
@PropertySource("classpath:")
@Slf4j
@RequiredArgsConstructor
public class MainScheduler {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final AnalyseRepository analyseRepository;
    private final UserService userService;
    private final OperationService operationService;
    private final QuotesService quotesService;
    private final AnalyseService analyseService;

    @Value("${ru.tinkoff.piapi.core.api.target}")
    private String target;

    @Value("${services.main-scheduler.max-threads}")
    private int maxThreads;

    private List<ExchangeUserService> exchangeUserServices;
    private List<Account> accounts;

    public void updateUsers() {
        var users = userRepository.findAll();
        var exchangeUserServices = getExchangeUserServices(users);
        var accounts = getAccounts(exchangeUserServices);
        analyseAccounts(accounts);
    }

    private List<ExchangeUserService> getExchangeUserServices(List<User> users) {
        exchangeUserServices = new ArrayList<>();
        users.forEach(user -> {
            var token = userService.decrypt(user.getToken());
            var interceptor = new AuthInterceptor(token);
            var channel = ManagedChannelBuilder.forTarget(target).useTransportSecurity().intercept(interceptor).build();
            var exchangeUserService = new ExchangeUserService(channel);
            exchangeUserService.setUser(user);
            exchangeUserServices.add(exchangeUserService);
            log.info("Make exchange user service {}", exchangeUserService);
        });
        return exchangeUserServices;
    }

    private List<Account> getAccounts(List<ExchangeUserService> exchangeUserServices) {
        accounts = new ArrayList<>();
        var futures = new ArrayList<CompletableFuture<Void>>();
        exchangeUserServices.forEach(exchangeUserService -> {
            futures.add(CompletableFuture.runAsync(() -> {
                accounts.addAll(exchangeUserService.getAccounts(AccountStatus.ACCOUNT_STATUS_ALL));
                log.info("Got accounts from exchange user service {}", exchangeUserService);
            }));
        });
        futures.forEach(CompletableFuture::join);
        accountRepository.saveAll(accounts);
        return accounts;
    }

    private void analyseAccounts(List<Account> accounts) {
        try (ExecutorService threadPool = Executors.newFixedThreadPool(maxThreads)) {
            for (var account : accounts) {
                // TODO: написать получение безрисковой ставки и бенчмарка от пользователя
                threadPool.submit(() -> analyseAccount(account, 0.08, 0.12));
            }
            threadPool.shutdown();
        }

        log.info("Accounts are analysed");
    }

    private void analyseAccount(Account account, Double riskFree, Double meanBenchmark) {
        var futures = new ArrayList<CompletableFuture<Void>>();
        var positions = operationService.getPositions(account.getId());

        // Анализируем только ценные бумаги. Фьючерсы и опционы не анализируем
        positions.getSecurities().forEach(securityPosition -> {
            futures.add(CompletableFuture.runAsync(() -> {
                var to = Instant.now();
                var from = to.minus(1, ChronoUnit.YEARS);
                var candles = quotesService.getHistoricCandles(securityPosition.getFigi(), to, from, CandleInterval.CANDLE_INTERVAL_DAY);
                var analyse = MapperEntities.AnalyseResponseToAnalyse(analyseService.analyse(new AnalyseRequest(candles, riskFree, meanBenchmark)));
                analyse.setSecuritiesUid(securityPosition.getFigi());
                analyseRepository.save(analyse);
            }));
        });
        futures.forEach(CompletableFuture::join);
        log.info("Account {} is analysed", account.getId());
    }


//    @Scheduled(cron = "0 50 9 * * 1-5")
//    public void startTrade() throws ExecutionException, InterruptedException {
//        var result = userServices.getAccounts(AccountStatus.ACCOUNT_STATUS_ALL);
//        System.out.println(result);
//    }
}
