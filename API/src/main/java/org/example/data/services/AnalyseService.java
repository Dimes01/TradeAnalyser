package org.example.data.services;

import lombok.extern.slf4j.Slf4j;
import org.example.data.dto.AnalyseRequest;
import org.example.data.dto.AnalyseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
public class AnalyseService {
    @Autowired private RestClient restClient;

    @Value("${services.analyse.url}")
    private String url;

    public AnalyseResponse analyse(AnalyseRequest request) {
        log.info("Method 'analyse': started");
        AnalyseResponse response = restClient.post()
            .uri(String.format("%s/analyse", url))
            .body(request)
            .retrieve()
            .body(AnalyseResponse.class);
        log.info("Method 'analyse': finished");
        return response;
    }
}
