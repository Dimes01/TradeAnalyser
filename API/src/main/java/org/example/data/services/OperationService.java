package org.example.data.services;

import io.grpc.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.models.Positions;

import java.util.concurrent.ExecutionException;

@Service
public class OperationService {

    private final Logger logger = LoggerFactory.getLogger(OperationService.class);
    private final InvestApi api;

    @Autowired
    public OperationService(Channel channel) {
        this.api = InvestApi.createReadonly(channel);
    }

    public Positions getPositions(String idAccount) throws ExecutionException, InterruptedException {
        logger.info("Method 'getPositions': started");
        var result = api.getOperationsService().getPositions(idAccount);
        logger.info("Method 'getPositions': finished");
        return result.get();
    }
}
