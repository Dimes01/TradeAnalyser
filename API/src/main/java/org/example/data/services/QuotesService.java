package org.example.data.services;

import io.grpc.Channel;
import org.example.data.dto.HistoricCandleDTO;
import org.example.data.dto.LastPriceDTO;
import org.example.data.utilities.MapperDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.core.InvestApi;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class QuotesService {
    private final Logger logger = LoggerFactory.getLogger(OperationService.class);
    private final InvestApi api;

    @Autowired
    public QuotesService(Channel channel) {
        this.api = InvestApi.createReadonly(channel);
    }

    public List<LastPriceDTO> getLastPrices(Iterable<String> idsInstruments) throws ExecutionException, InterruptedException {
        logger.info("Method 'getLastPrice': started");
        var result = api.getMarketDataService().getLastPrices(idsInstruments).get()
            .stream().map(MapperDTO::LastPriceToDTO).toList();
        logger.info("Method 'getLastPrice': finished");
        return result;
    }

    public List<HistoricCandleDTO> getHistoricCandles(String idInstrument, Instant from, Instant to, CandleInterval interval)
        throws ExecutionException, InterruptedException {
        logger.info("Method 'getCandles': started");
        var result = api.getMarketDataService().getCandles(idInstrument, from, to, interval).get()
            .stream().map(MapperDTO::HistoricCandleToDTO).toList();
        logger.info("Method 'getCandles': finished");
        return result;
    }
}
