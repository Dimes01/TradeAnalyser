package org.example.auth.services;

import lombok.RequiredArgsConstructor;
import org.example.auth.dto.LoginRequest;
import org.example.auth.dto.LoginResponse;
import org.example.auth.dto.RegisterRequest;
import org.example.auth.models.User;
import org.example.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:local.properties")
public class UserService {
    private final JWTService jwtService;
    private final AuthenticationManager authManager;
    private final UserRepository repo;

    @Value("${jwt.secret}")
    private String KEY_ENV_VAR;

    private final String ALGORITHM = "AES";
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void register(RegisterRequest request) throws Exception {
        repo.save(new User(request.getUsername(), encoder.encode(request.getPassword()), encrypt(request.getApiKey())));
    }

    public LoginResponse verify(LoginRequest request) {
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (authentication.isAuthenticated()) {
            return new LoginResponse(jwtService.generateToken(request.getUsername()));
        } else {
            return null;
        }
    }

    public String encrypt(String valueToEnc) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        return Base64.getEncoder().encodeToString(encValue);
    }

    public String decrypt(String encryptedValue) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decValue = Base64.getDecoder().decode(encryptedValue);
        byte[] decryptedValue = c.doFinal(decValue);
        return new String(decryptedValue);
    }

    private Key generateKey() {
        return new SecretKeySpec(KEY_ENV_VAR.getBytes(), ALGORITHM);
    }
}
