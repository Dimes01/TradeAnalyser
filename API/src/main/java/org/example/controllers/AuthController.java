package org.example.controllers;

import jakarta.servlet.ServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.auth.LoginRequest;
import org.example.dto.auth.LoginResponse;
import org.example.dto.auth.RegisterRequest;
import org.example.dto.auth.RegisterResponse;
import org.example.services.auth.UserService;
import org.example.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AuthController {
    private final UserService service;
    private final AccountService accountService;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Endpoint /api/user/register is started");
        var user = service.register(request);
        var response = new RegisterResponse(user.getUsername(), true, false);
        response.setSuccessGetAccounts(accountService.updateAccountsByApiKey(request.getApiKey(), user));
        log.info("Endpoint /api/user/register is finished");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Endpoint /api/user/login is started");
        var response = service.verify(request);
        log.info("Endpoint /api/user/login is finished");
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/token/{username}/{token}")
    public ResponseEntity<Void> changeToken(
        @PathVariable @NotBlank String username,
        @PathVariable @NotBlank String token
    ) {
        log.info("Endpoint /api/user/token/.../... is started");
        service.changeToken(username, token);
        log.info("Endpoint /api/user/token/.../... is finished");
        return ResponseEntity.ok().build();
    }
}