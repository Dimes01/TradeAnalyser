package org.example.data.configurations;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import org.example.data.utilities.AuthInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:exchange.properties", "classpath:local.properties" })
public class ExchangeServiceConfiguration {

    @Value("${ru.tinkoff.piapi.core.api.target}")
    private String target;

    @Value("${ru.tinkoff.piapi.core.api.token}")
    private String token;

    private Channel channel;

    @PostConstruct
    public void init() {
        AuthInterceptor authInterceptor = new AuthInterceptor(token);
        channel = ManagedChannelBuilder
            .forTarget(target)
            .useTransportSecurity()
            .intercept(authInterceptor)
            .build();
    }

    @Bean
    public Channel getChannel() { return channel; }
}
