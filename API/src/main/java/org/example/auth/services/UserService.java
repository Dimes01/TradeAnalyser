package org.example.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.auth.dto.LoginRequest;
import org.example.auth.dto.LoginResponse;
import org.example.auth.dto.RegisterRequest;
import org.example.auth.dto.RegisterResponse;
import org.example.auth.models.User;
import org.example.auth.repositories.UserRepository;
import org.example.auth.utilities.CryptUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:local.properties")
public class UserService {
    private final JWTService jwtService;
    private final AuthenticationManager authManager;
    private final UserRepository repo;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public RegisterResponse register(RegisterRequest request) {
        try {
            var user = new User(request.getUsername(), encoder.encode(request.getPassword()), CryptUtil.encrypt(request.getApiKey()));
            repo.save(user);
            return new RegisterResponse(user.getUsername(), true, false);
        } catch (Exception e) {
            log.error("Method 'register': could not register user with username '{}'", request.getUsername());
            return new RegisterResponse(request.getUsername(), false, false);
        }
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
