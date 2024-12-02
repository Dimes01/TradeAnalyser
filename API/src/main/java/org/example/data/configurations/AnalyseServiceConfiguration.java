package org.example.data.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AnalyseServiceConfiguration {
    @Value("${services.analyse.base-url}")
    private String baseUrl;

    @Bean
    public RestClient restClient() { return RestClient.builder().baseUrl(baseUrl).build(); }
}
