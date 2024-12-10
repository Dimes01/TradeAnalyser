package org.example.services.t_api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ShareDTO;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InvestApi;

@Slf4j
@RequiredArgsConstructor
public class InstrumentsService_T_API {
    private final InvestApi api;

    public Share getShareByUid(String uid) {
        return api.getInstrumentsService().getShareByUidSync(uid);
    }
}
