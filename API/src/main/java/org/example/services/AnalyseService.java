package org.example.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.internal.AnalyseRequest;
import org.example.dto.internal.AnalyseResponse;
import org.example.entities.Analyse;
import org.example.repositories.AnalyseRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnalyseService {
    private final RestClient restClient;
    private final AnalyseRepository analyseRepository;
    private final ObjectMapper utilMapper = new ObjectMapper();

    public AnalyseResponse analyse(AnalyseRequest request) {
        log.info("Method 'analyse': started");
        AnalyseResponse response = null;
        try {
            String body = utilMapper.writeValueAsString(request);
            log.info(body);
            response = restClient.post()
                .uri("/analyse")
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(AnalyseResponse.class);
        } catch (JsonProcessingException e) {
            log.warn("Could not serialize request");
        }
        log.info("Method 'analyse': finished");
        return response;
    }

    public void saveAll(Iterable<Analyse> analysis) {
        analyseRepository.saveAll(analysis);
    }
}
