package org.example.services.t_api;

import io.grpc.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.models.Positions;

import java.util.concurrent.ExecutionException;

// Данная аннотация закомментирована из-за того, что возникало "APPLICATION FAILED TO START", потому что для
// конструктора сервиса требовался bean Channel, а так как Channel для каждого пользователя свой, то этот бин не создавался
// Точно так же аннотация закомментирована для других сервисов, которые используются для обращения по GRPC к T-Invest API
//@Service

@Slf4j
@RequiredArgsConstructor
public class OperationService_T_API {
    private final InvestApi api;

    public Positions getPositions(String idAccount) {
        log.info("Method 'getPositions': started");
        Positions result = null;
        try {
            result = api.getOperationsService().getPositions(idAccount).get();
        } catch (InterruptedException e) {
            log.error("Method 'getPositions': interrupted thread while waiting positions for account {}", idAccount);
        } catch (ExecutionException e) {
            log.error("Method 'getPositions': this future completed exceptionally for get positions for account {}", idAccount);
        }
        log.info("Method 'getPositions': finished");
        return result;
    }
}
