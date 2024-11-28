package org.example.data.services;

import io.grpc.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.List;

@Service
public class InstrumentsService {
    private final Logger logger = LoggerFactory.getLogger(OperationService.class);
    private final InvestApi api;

    @Autowired
    public InstrumentsService(Channel channel) {
        this.api = InvestApi.createReadonly(channel);
    }

    public List<Share> getTradableShares() {
        logger.info("Method 'getShares': start");
        var shares = api.getInstrumentsService().getTradableSharesSync();
        logger.info("Method 'getShares': finish");
        return shares;
    }
}
