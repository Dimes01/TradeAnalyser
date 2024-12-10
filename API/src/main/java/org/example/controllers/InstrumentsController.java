package org.example.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ShareDTO;
import org.example.services.InstrumentService;
import org.example.services.t_api.InstrumentsService_T_API;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/instruments")
@RequiredArgsConstructor
public class InstrumentsController {
    private final InstrumentService instrumentsService;

    @GetMapping("/shares/{uid}")
    public ResponseEntity<ShareDTO> getShareByUid(@PathVariable @NotBlank String uid) {
        return ResponseEntity.ok(instrumentsService.getShareByUid(uid));
    }
}
