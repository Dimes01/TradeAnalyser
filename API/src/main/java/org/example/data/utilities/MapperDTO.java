package org.example.data.utilities;

import org.example.data.dto.AccountDTO;
import org.example.data.dto.HistoricCandleDTO;
import org.example.data.dto.LastPriceDTO;
import ru.tinkoff.piapi.contract.v1.Account;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.contract.v1.LastPrice;
import ru.tinkoff.piapi.core.utils.DateUtils;
import ru.tinkoff.piapi.core.utils.MapperUtils;

import java.time.Instant;

public class MapperDTO {
    public static AccountDTO AccountToDTO(Account account) {
        return new AccountDTO(
            account.getId(),
            account.getTypeValue(),
            account.getName(),
            account.getStatusValue(),
            DateUtils.timestampToInstant(account.getOpenedDate()),
            DateUtils.timestampToInstant(account.getClosedDate()),
            account.getAccessLevelValue()
        );
    }

    public static LastPriceDTO LastPriceToDTO(LastPrice lastPrice) {
        return new LastPriceDTO(
            lastPrice.getFigi(),
            lastPrice.getPrice().getUnits(),
            lastPrice.getPrice().getNano(),
            DateUtils.timestampToInstant(lastPrice.getTime()),
            lastPrice.getInstrumentUid(),
            lastPrice.getLastPriceTypeValue()
        );
    }

    public static HistoricCandleDTO HistoricCandleToDTO(HistoricCandle historicCandle) {
        return new HistoricCandleDTO(
            MapperUtils.quotationToBigDecimal(historicCandle.getOpen()),
            MapperUtils.quotationToBigDecimal(historicCandle.getClose()),
            MapperUtils.quotationToBigDecimal(historicCandle.getLow()),
            MapperUtils.quotationToBigDecimal(historicCandle.getHigh()),
            historicCandle.getVolume(),
            DateUtils.timestampToInstant(historicCandle.getTime()),
            historicCandle.getIsComplete(),
            historicCandle.getCandleSourceValue()
        );
    }
}
