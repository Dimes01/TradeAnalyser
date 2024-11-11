package org.example.controllers;

import jakarta.security.auth.message.AuthException;
import lombok.RequiredArgsConstructor;
import org.example.dto.JwtRequest;
import org.example.dto.JwtResponse;
import org.example.dto.RefreshJwtRequest;
import org.example.services.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthenticationService authService;

    @PostMapping("login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest authRequest) throws AuthException {
        logger.info("Endpoint /api/auth/login: start");
        JwtResponse token = authService.login(authRequest);
        logger.info("Endpoint /api/auth/login: finish");
        return ResponseEntity.ok(token);
    }

    @PostMapping("token")
    public ResponseEntity<JwtResponse> getNewAccessToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        logger.info("Endpoint /api/auth/token: start");
        JwtResponse token = authService.getAccessToken(request.getRefreshToken());
        logger.info("Endpoint /api/auth/token: finish");
        return ResponseEntity.ok(token);
    }

    @PostMapping("refresh")
    public ResponseEntity<JwtResponse> getNewRefreshToken(@RequestBody RefreshJwtRequest request) throws AuthException {
        logger.info("Endpoint /api/auth/refresh: start");
        JwtResponse token = authService.refresh(request.getRefreshToken());
        logger.info("Endpoint /api/auth/refresh: finish");
        return ResponseEntity.ok(token);
    }

}
