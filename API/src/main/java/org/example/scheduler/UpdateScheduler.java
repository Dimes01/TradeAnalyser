package org.example.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.Account;
import org.example.entities.User;
import org.example.repositories.UserRepository;
import org.example.services.AccountService;
import org.example.utilities.CryptUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;

@Component
@Slf4j
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
public class UpdateScheduler {
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final CryptUtil cryptUtil;

    @Value("${services.main-scheduler.max-threads}")
    private int maxThreads;

    @Scheduled(cron = "0 * * * * *")
    public void update() {
        log.info("Update scheduler started");
        var users = userRepository.findAll();
        var futures = new LinkedList<CompletableFuture<Void>>();
        try (var userExecutor = Executors.newFixedThreadPool(maxThreads)) {
            users.forEach(user -> {
                futures.add(CompletableFuture.runAsync(() -> {
                    updateUser(user);
                }, userExecutor));
            });
        }
        futures.forEach(CompletableFuture::join);
        log.info("Update scheduler finished");
    }

    private void updateUser(User user) {
        String token = null;
        try {
            token = cryptUtil.decrypt(user.getToken());
        } catch (Exception e) {
            log.error("Error while decrypting token");
        }
        if (accountService.updateAccountsByApiKey(token))
            log.info("Successfully updated accounts of user!");
        else
            log.warn("Not successfully updated accounts of user!");
    }
}
