package org.example.services.auth;

import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.auth.LoginRequest;
import org.example.dto.auth.LoginResponse;
import org.example.dto.auth.RegisterRequest;
import org.example.dto.auth.RegisterResponse;
import org.example.entities.User;
import org.example.repositories.UserRepository;
import org.example.utilities.CryptUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:local.properties")
public class UserService {
    @Value("${spring.security.password-encoder.strength}")
    private int strengthEncoder;

    private final JWTService jwtService;
    private final CryptUtil cryptUtil;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final UserRepository repo;


    public User register(RegisterRequest request) {
        var user = repo.findByUsername(request.getUsername());
        if (user != null)
            throw new EntityExistsException(String.format("Username '%s' already exists!", request.getUsername()));
        user = new User(request.getUsername(), encoder.encode(request.getPassword()), cryptUtil.encrypt(request.getApiKey()));
        repo.save(user);
        return user;
    }

    public LoginResponse verify(LoginRequest request) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (authentication.isAuthenticated()) {
            return new LoginResponse(jwtService.generateToken(request.getUsername()), true);
        } else {
            return new LoginResponse(null, false);
        }
    }
}
