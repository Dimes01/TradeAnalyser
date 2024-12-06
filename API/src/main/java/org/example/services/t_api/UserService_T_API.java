package org.example.services.t_api;

import io.grpc.Channel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.example.entities.User;
import org.example.entities.Account;
import org.example.utilities.MapperEntities;
import ru.tinkoff.piapi.contract.v1.AccountStatus;
import ru.tinkoff.piapi.core.InvestApi;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

// Данная аннотация закомментирована из-за того, что возникало "APPLICATION FAILED TO START", потому что для
// конструктора сервиса требовался bean Channel, а так как Channel для каждого пользователя свой, то этот бин не создавался
// Точно так же аннотация закомментирована для других сервисов, которые используются для обращения по GRPC к T-Invest API
//@Service

@Slf4j
public class UserService_T_API {

    private final InvestApi api;
    @Getter @Setter private User user;

    public UserService_T_API(Channel channel) {
        this.api = InvestApi.createReadonly(channel);
    }

    public List<Account> getAccounts(AccountStatus status) {
        log.info("Method 'getAccounts': started");
        var result = api.getUserService().getAccounts(status);
        List<Account> accounts = Collections.emptyList();
        try {
            accounts = result.get()
                .stream()
                .map((account) -> {
                    var accountEntity = MapperEntities.AccountToEntity(account);
                    accountEntity.setUser(user);
                    return accountEntity;
                })
                .toList();
        } catch (InterruptedException e) {
            log.error("Method 'getAccounts': interrupted thread while waiting exchange user service {}", this);
        } catch (ExecutionException e) {
            log.error("Method 'getAccounts': this future completed exceptionally for exchange user service {}", this);
        }
        log.info("Method 'getAccounts': finished");
        return accounts;
    }
}
