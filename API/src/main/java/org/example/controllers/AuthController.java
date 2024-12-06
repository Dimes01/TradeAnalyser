package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.dto.auth.LoginRequest;
import org.example.dto.auth.LoginResponse;
import org.example.dto.auth.RegisterRequest;
import org.example.dto.auth.RegisterResponse;
import org.example.services.auth.UserService;
import org.example.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService service;
    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) throws Exception {
        var response = service.register(request);
        if (response.isSuccessRegister())
            response.setSuccessGetAccounts(accountService.updateAccountsByApiKey(request.getApiKey()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.verify(request));
    }
}