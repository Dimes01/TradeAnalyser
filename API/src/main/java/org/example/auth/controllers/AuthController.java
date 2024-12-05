package org.example.auth.controllers;

import lombok.RequiredArgsConstructor;
import org.example.auth.dto.LoginRequest;
import org.example.auth.dto.LoginResponse;
import org.example.auth.dto.RegisterRequest;
import org.example.auth.dto.RegisterResponse;
import org.example.auth.services.UserService;
import org.example.data.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
            response.setSuccessGetAccounts(accountService.updateAccountsByUsername(request.getUsername(), request.getApiKey()));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(service.verify(request));
    }
}