package org.example.services.t_api;

import io.grpc.Channel;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.piapi.contract.v1.Share;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.List;

// Данная аннотация закомментирована из-за того, что возникало "APPLICATION FAILED TO START", потому что для
// конструктора сервиса требовался bean Channel, а так как Channel для каждого пользователя свой, то этот бин не создавался
// Точно так же аннотация закомментирована для других сервисов, которые используются для обращения по GRPC к T-Invest API
//@Service

@Slf4j
public class InstrumentsService_T_API {
    private final InvestApi api;

    public InstrumentsService_T_API(Channel channel) {
        this.api = InvestApi.createReadonly(channel);
    }

    public List<Share> getTradableShares() {
        log.info("Method 'getShares': start");
        var shares = api.getInstrumentsService().getTradableSharesSync();
        log.info("Method 'getShares': finish");
        return shares;
    }
}
