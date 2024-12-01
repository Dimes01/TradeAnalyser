package org.example.data.services;

import io.grpc.Channel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.models.Positions;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class OperationService {

    private final Logger logger = LoggerFactory.getLogger(OperationService.class);
    private final InvestApi api;

    @Autowired
    public OperationService(Channel channel) {
        this.api = InvestApi.createReadonly(channel);
    }

    public Positions getPositions(String idAccount) {
        logger.info("Method 'getPositions': started");
        Positions result = null;
        try {
            result = api.getOperationsService().getPositions(idAccount).get();
        } catch (InterruptedException e) {
            log.error("Method 'getPositions': interrupted thread while waiting positions for account {}", idAccount);
        } catch (ExecutionException e) {
            log.error("Method 'getPositions': this future completed exceptionally for get positions for account {}", idAccount);
        }
        logger.info("Method 'getPositions': finished");
        return result;
    }
}
