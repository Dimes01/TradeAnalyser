package org.example.utilities;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@PropertySource("classpath:exchange.properties")
@Component
public class Channels {

    @Value("${ru.tinkoff.piapi.core.api.target}")
    private String target;

    public Channel withEncryptedToken(String encryptedToken) throws Exception {
        var decryptToken = CryptUtil.decrypt(encryptedToken);
        return withDecryptedToken(decryptToken);
    }

    public Channel withDecryptedToken(String decryptedToken) {
        var interceptor = new AuthInterceptor(decryptedToken);
        return ManagedChannelBuilder.forTarget(target).useTransportSecurity().intercept(interceptor).build();
    }
}
