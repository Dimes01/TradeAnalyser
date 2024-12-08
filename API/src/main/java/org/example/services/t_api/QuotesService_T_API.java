package org.example.services.t_api;

import io.grpc.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.HistoricCandleDTO;
import org.example.dto.LastPriceDTO;
import org.example.utilities.MapperDTO;
import ru.tinkoff.piapi.contract.v1.CandleInterval;
import ru.tinkoff.piapi.contract.v1.GetCandlesRequest;
import ru.tinkoff.piapi.core.InvestApi;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

// Данная аннотация закомментирована из-за того, что возникало "APPLICATION FAILED TO START", потому что для
// конструктора сервиса требовался bean Channel, а так как Channel для каждого пользователя свой, то этот бин не создавался
// Точно так же аннотация закомментирована для других сервисов, которые используются для обращения по GRPC к T-Invest API
//@Service

@Slf4j
@RequiredArgsConstructor
public class QuotesService_T_API {
    private final InvestApi api;

    public List<LastPriceDTO> getLastPrices(Iterable<String> idsInstruments) throws ExecutionException, InterruptedException {
        log.info("Method 'getLastPrice': started");
        var result = api.getMarketDataService().getLastPrices(idsInstruments).get()
            .stream().map(MapperDTO::LastPriceToDTO).toList();
        log.info("Method 'getLastPrice': finished");
        return result;
    }

    public List<HistoricCandleDTO> getHistoricCandles(String idInstrument, Instant from, Instant to, CandleInterval interval) {
        log.info("Method 'getHistoricCandles': started");
        List<HistoricCandleDTO> result = Collections.emptyList();
        try {
            result = api.getMarketDataService().getCandles(
                    idInstrument, from, to, interval,
                    GetCandlesRequest.CandleSource.CANDLE_SOURCE_EXCHANGE).get()
                .stream().map(MapperDTO::HistoricCandleToDTO).toList();
        } catch (InterruptedException e) {
            log.error("Method 'getHistoricCandles': interrupted thread while waiting historic candles for instrument with id: {}", idInstrument);
        } catch (ExecutionException e) {
            log.error("Method 'getHistoricCandles': this future completed exceptionally for get historic candles for instrument with id: {}", idInstrument);
        }
        log.info("Method 'getHistoricCandles': finished");
        return result;
    }
}
