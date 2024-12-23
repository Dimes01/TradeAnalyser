package org.example.services.t_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InvestApi;

@Slf4j
@RequiredArgsConstructor
public class InstrumentsService_T_API {
    private final InvestApi api;

    public Share getShareByFigi(String figi) {
        log.info("Method 'getShareByFigi' from T-Invest API is started");
        var share = api.getInstrumentsService().getShareByFigiSync(figi);
        log.info("Method 'getShareByFigi' from T-Invest API is finished");
        return share;
    }
}
