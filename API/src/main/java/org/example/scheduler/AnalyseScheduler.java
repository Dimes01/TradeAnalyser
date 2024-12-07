package org.example.scheduler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.internal.AnalyseRequest;
import org.example.entities.Account;
import org.example.entities.Analyse;
import org.example.repositories.AccountRepository;
import org.example.services.AnalyseService;
import org.example.services.t_api.OperationService_T_API;
import org.example.services.t_api.QuotesService_T_API;
import org.example.services.SettingsService;
import org.example.utilities.Channels;
import org.example.utilities.MapperEntities;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.piapi.contract.v1.CandleInterval;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.concurrent.*;

@Component
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:local.properties")
@PropertySource("classpath:exchange.properties")
@PropertySource("classpath:application.yml")
public class AnalyseScheduler {
    private final AnalyseService analyseService;
    private final SettingsService settingsService;
    private final AccountRepository accountRepository;
    private final Channels channels;

    private OperationService_T_API operationService;
    private QuotesService_T_API quotesService;

    @Value("${services.main-scheduler.max-threads}")
    private int maxThreads;

    @Value("${ru.tinkoff.piapi.core.api.token}")
    private String commonToken;

    @PostConstruct
    public void init() {
        operationService = new OperationService_T_API(channels.withDecryptedToken(commonToken));
        quotesService = new QuotesService_T_API(channels.withDecryptedToken(commonToken));
    }

    @Scheduled(fixedRate = 60000)
    public void analyse() {
        log.info("Analyse scheduler started");
        var accounts = accountRepository.findAll();
        var futures = new LinkedList<CompletableFuture<Void>>();
        try(var accountExecutor = Executors.newFixedThreadPool(maxThreads)) {
            for (var account : accounts) {
                futures.add(CompletableFuture.runAsync(() -> analyseAccount(account), accountExecutor));
            }
            futures.forEach(CompletableFuture::join);
        }
        log.info("Analyse scheduler finished");
    }

    // TODO: сделать вызовы в БД и API асинхронными
    private void analyseAccount(Account account) {
        log.info("Analyse account started...");
        var settings = settingsService.findByAccountId(account.getId());
        var securities = operationService.getPositions(account.getId()).getSecurities();
        var analysis = new ConcurrentLinkedDeque<Analyse>();
        var futures = new LinkedList<CompletableFuture<Void>>();
        log.info("Start analyse securities...");
        securities.forEach(security -> {
            futures.add(CompletableFuture.runAsync(() -> {
                var now = Instant.now();
                var from = now.minus(365, ChronoUnit.DAYS);
                var candles = quotesService.getHistoricCandles(security.getFigi(), from, now, CandleInterval.CANDLE_INTERVAL_DAY);
                var request = new AnalyseRequest(candles, settings.getFiskFree(), settings.getMeanBenchmark());
                var response = analyseService.analyse(request);
                var analyse = MapperEntities.AnalyseResponseToAnalyse(response, from, now, account, security.getFigi());
                analysis.add(analyse);
            }));
        });
        futures.forEach(CompletableFuture::join);
        log.info("Finish analyse securities!");
        analyseService.saveAll(analysis);
        log.info("Analyse is saved in database!");
        log.info("Analyse account finished!");
    }
}
