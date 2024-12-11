package org.example.utilities;

import org.example.dto.internal.HistoricCandleDTO;
import org.example.dto.LastPriceDTO;
import ru.tinkoff.piapi.contract.v1.HistoricCandle;
import ru.tinkoff.piapi.contract.v1.LastPrice;
import ru.tinkoff.piapi.core.utils.DateUtils;
import ru.tinkoff.piapi.core.utils.MapperUtils;

public class MapperDTO {

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
            DateUtils.timestampToInstant(historicCandle.getTime()),
            historicCandle.getIsComplete(),
            historicCandle.getVolume(),
            historicCandle.getCandleSourceTypeValue()
        );
    }
}
