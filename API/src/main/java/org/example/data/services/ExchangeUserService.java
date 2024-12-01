package org.example.data.services;

import io.grpc.Channel;
import lombok.extern.slf4j.Slf4j;
import org.example.data.dto.AccountDTO;
import org.example.data.utilities.MapperDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.AccountStatus;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class ExchangeUserService {
    private final Logger logger = LoggerFactory.getLogger(ExchangeUserService.class);
    private final InvestApi api;

    @Autowired
    public ExchangeUserService(Channel channel) {
        this.api = InvestApi.createReadonly(channel);
    }

    public List<AccountDTO> getAccounts(AccountStatus status) {
        logger.info("Method 'getAccounts': started");
        var result = api.getUserService().getAccounts(status);
        List<AccountDTO> accounts = null;
        try {
            accounts = result.get()
                .stream()
                .map(MapperDTO::AccountToDTO)
                .toList();
        } catch (InterruptedException e) {
            log.error("Interrupted thread while waiting exchange user service {}", this);
        } catch (ExecutionException e) {
            log.error("This future completed exceptionally for exchange user service {}", this);
        }
        logger.info("Method 'getAccounts': finished");
        return accounts;
    }
}
