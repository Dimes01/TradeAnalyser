package org.example.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ShareDTO;
import org.example.services.t_api.InstrumentsService_T_API;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InvestApi;


@Slf4j
@Service
@PropertySource("classpath:local.properties")
@RequiredArgsConstructor
public class InstrumentService {

    @Value("${ru.tinkoff.piapi.core.api.token}")
    private String commonToken;

    private InstrumentsService_T_API instrumentsService_t_api;

    @PostConstruct
    public void postConstruct() {
        instrumentsService_t_api = new InstrumentsService_T_API(InvestApi.createReadonly(commonToken));
    }

    public ShareDTO getShareByUid(String uid) {
        return ShareDTO.fromResponse(instrumentsService_t_api.getShareByUid(uid));
    }
}
