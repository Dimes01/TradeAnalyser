package org.example.auth.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public String decrypt(String encryptedValue) {
        byte[] decryptedValue = new byte[0];
        try {
            Cipher c = Cipher.getInstance(ALGORITHM);
            c.init(Cipher.DECRYPT_MODE, generateKey());
            byte[] decValue = Base64.getDecoder().decode(encryptedValue);
            decryptedValue = c.doFinal(decValue);
        } catch (NoSuchAlgorithmException e) {
            log.error("Method 'decrypt': algorithm not found");
        } catch (NoSuchPaddingException e) {
            log.error("Method 'decrypt': could not get instance of cipher");
        } catch (InvalidKeyException e) {
            log.error("Method 'decrypt': key is invalid");
        } catch (IllegalBlockSizeException e) {
            log.error("Method 'decrypt': illegal block size");
        } catch (BadPaddingException e) {
            log.error("Method 'decrypt': bad padding");
        }
        return new String(decryptedValue);
    }

    private Key generateKey() {
        return new SecretKeySpec(KEY_ENV_VAR.getBytes(), ALGORITHM);
    }
}
