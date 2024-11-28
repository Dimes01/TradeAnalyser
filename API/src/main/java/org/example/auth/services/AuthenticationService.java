package org.example.auth.services;

import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.auth.dto.JwtResponse;
import org.example.auth.entities.User;
import org.example.auth.models.JwtAuthentication;
import org.example.auth.utilities.JwtProvider;
import org.example.auth.dto.JwtRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthenticationService {
    private final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);
    private final UserService userService;
    private final Map<String, String> refreshStorage;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthenticationService(
        @Qualifier("auth-user-service") UserService userService,
        Map<String, String> refreshStorage,
        JwtProvider jwtProvider) {
        this.userService = userService;
        this.refreshStorage = refreshStorage;
        this.jwtProvider = jwtProvider;
    }

    public JwtResponse login(@NonNull JwtRequest authRequest) throws AuthException {
        logger.info("Method 'login': start");
        User user = userService.getByLogin(authRequest.getLogin())
            .orElseThrow(() -> {
                logger.error("Method 'login': user not found");
                return new AuthException("Пользователь не найден");
            });
        if (user.getPassword().equals(authRequest.getPassword())) {
            logger.debug("Method 'login': password is correct");
            String accessToken = jwtProvider.generateAccessToken(user);
            String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getLogin(), refreshToken);
            logger.info("Method 'login': finish");
            return new JwtResponse(accessToken, refreshToken);
        } else {
            logger.error("Method 'login': wrong password");
            throw new AuthException("Неправильный пароль");
        }
    }

    public JwtResponse getAccessToken(@NonNull String refreshToken) throws AuthException {
        logger.info("Method 'getAccessToken': start");
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            logger.debug("Method 'getAccessToken': refresh token is valid");
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                logger.debug("Method 'getAccessToken': refresh token from storage is valid");
                User user = userService.getByLogin(login)
                    .orElseThrow(() -> {
                        logger.error("Method 'getAccessToken': user not found");
                        return new AuthException("Пользователь не найден");
                    });
                String newAccessToken = jwtProvider.generateAccessToken(user);
                logger.info("Method 'getAccessToken': finish with new access token");
                return new JwtResponse(newAccessToken, refreshToken);
            }
        }
        logger.info("Method 'getAccessToken': finish with null tokens");
        return new JwtResponse(null, null);
    }

    public JwtResponse refresh(@NonNull String refreshToken) throws AuthException {
        logger.info("Method 'refresh': start");
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            logger.debug("Method 'refresh': refresh token is valid");
            Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            String login = claims.getSubject();
            String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                logger.debug("Method 'refresh': refresh token from storage is valid");
                User user = userService.getByLogin(login)
                    .orElseThrow(() -> {
                        logger.error("Method 'refresh': user not found");
                        return new AuthException("Пользователь не найден");
                    });
                String newAccessToken = jwtProvider.generateAccessToken(user);
                String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getLogin(), newRefreshToken);
                logger.info("Method 'refresh': finish with new tokens");
                return new JwtResponse(newAccessToken, newRefreshToken);
            }
        }
        logger.info("Method 'refresh': finish with null tokens");
        throw new AuthException("Невалидный JWT токен");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
