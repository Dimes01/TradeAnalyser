package org.example.data.utilities;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.example.auth.utilities.CryptUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:")
public class Channels {

    @Value("${ru.tinkoff.piapi.core.api.target}")
    private static String target;

    public static Channel withEncryptedToken(String encryptedToken) throws Exception {
        var decryptToken = CryptUtil.decrypt(encryptedToken);
        return withDecryptedToken(decryptToken);
    }

    public static Channel withDecryptedToken(String decryptedToken) {
        var interceptor = new AuthInterceptor(decryptedToken);
        return ManagedChannelBuilder.forTarget(target).useTransportSecurity().intercept(interceptor).build();
    }
}
