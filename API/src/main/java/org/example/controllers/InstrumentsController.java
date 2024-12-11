package org.example.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ShareDTO;
import org.example.services.InstrumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/instruments")
@RequiredArgsConstructor
public class InstrumentsController {
    private final InstrumentService instrumentsService;

    // Получить информацию о бумаге невозможно, из-за того, что классы-модели, предоставляемые в JDK используют
    // устаревший идентификатор FIGI, который судя по документации к T-Invest API не рекомендуется к использованию.
    // Попытка использовать FIGI приводит к ошибке о неправильном идентификаторе, хотя идентификатор получен от T-Invest API

//    @GetMapping("/shares/{figi}")
//    public ResponseEntity<ShareDTO> getShareByFigi(@PathVariable @NotBlank String figi) {
//        log.info("Endpoint /api/instruments/shares/{figi} is started");
//        var share = instrumentsService.getShareByFigi(figi);
//        log.info("Endpoint /api/instruments/shares/{figi} is finished");
//        return ResponseEntity.ok(share);
//    }
}
