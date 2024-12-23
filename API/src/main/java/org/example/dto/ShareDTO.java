package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.models.Money;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShareDTO {
    private String figi;
    private String ticker;
    private String classCode;
    private String isin;
    private String uid;
    private String position_uid;
    private String asset_uid;

    private int lot;
    private String currency;
    private boolean short_enabled_flag;
    private String exchange;
    private String sector;
    private Money nominal;
    private boolean buy_available_flag;
    private boolean sell_available_flag;
    private boolean api_trade_available_flag;

    public static ShareDTO fromResponse(Share share) {
        return ShareDTO.builder()
            .figi(share.getFigi())
            .ticker(share.getTicker())
            .classCode(share.getClassCode())
            .isin(share.getIsin())
            .uid(share.getUid())
            .position_uid(share.getPositionUid())
            .asset_uid(share.getAssetUid())
            .lot(share.getLot())
            .currency(share.getCurrency())
            .short_enabled_flag(share.getShortEnabledFlag())
            .exchange(share.getExchange())
            .sector(share.getSector())
            .nominal(Money.fromResponse(share.getNominal()))
            .buy_available_flag(share.getBuyAvailableFlag())
            .sell_available_flag(share.getSellAvailableFlag())
            .api_trade_available_flag(share.getApiTradeAvailableFlag())
            .build();
    }
}
