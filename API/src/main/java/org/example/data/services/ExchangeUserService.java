package org.example.data.services;

import io.grpc.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.auth.models.User;
import org.example.data.entities.Account;
import org.example.data.utilities.MapperDTO;
import org.example.data.utilities.MapperEntities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.AccountStatus;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class ExchangeUserService {
    private final Logger logger = LoggerFactory.getLogger(ExchangeUserService.class);
    private final InvestApi api;
    @Setter @Getter private User user;

    public ExchangeUserService(@Autowired Channel channel) {
        this.api = InvestApi.createReadonly(channel);
    }

    public List<Account> getAccounts(AccountStatus status) {
        logger.info("Method 'getAccounts': started");
        var result = api.getUserService().getAccounts(status);
        List<Account> accounts = Collections.emptyList();
        try {
            accounts = result.get()
                .stream()
                .map((account) -> {
                    var accountEntity = MapperEntities.AccountToEntity(account);
                    accountEntity.setUser(user);
                    return accountEntity;
                })
                .toList();
        } catch (InterruptedException e) {
            log.error("Method 'getAccounts': interrupted thread while waiting exchange user service {}", this);
        } catch (ExecutionException e) {
            log.error("Method 'getAccounts': this future completed exceptionally for exchange user service {}", this);
        }
        logger.info("Method 'getAccounts': finished");
        return accounts;
    }
}
