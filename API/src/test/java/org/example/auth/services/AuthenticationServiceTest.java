package org.example.auth.services;

import io.jsonwebtoken.Claims;
import jakarta.security.auth.message.AuthException;
import org.example.auth.dto.JwtRequest;
import org.example.auth.dto.JwtResponse;
import org.example.auth.entities.User;
import org.example.auth.utilities.JwtProvider;
import org.example.auth.models.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class AuthenticationServiceTest {
    @Mock private AuthUserService authUserService;
    @Mock private JwtProvider jwtProvider;
    @Mock private HashMap<String, String> refreshStorage = new HashMap<>();
    @InjectMocks private AuthenticationService authenticationService;

    private final User user = new User("user", "1234", "Firstname", "Lastname", Collections.singleton(Role.USER));
    private final User admin = new User("admin", "12345", "Firstname", "Lastname", Collections.singleton(Role.ADMIN));
    private AutoCloseable openMocks;

    @BeforeEach
    void init() {
        openMocks = openMocks(this);
    }

    @AfterEach
    void close() throws Exception {
        openMocks.close();
    }

    static Stream<Arguments> invalidateSavedRefreshToken() {
        return Stream.of(
            Arguments.of("oldToken"),
            Arguments.of((Object) null)
        );
    }

    @Test
    void login_nullRequest_nullPointerException() {
        assertThrows(NullPointerException.class, () -> authenticationService.login(null));
    }

    @Test
    void login_existedUser_jwtResponse() throws AuthException {
        // Arrange
        JwtRequest request = new JwtRequest(user.getLogin(), user.getPassword());
        var expectedJwtResponse = new JwtResponse("generatedAccessToken", "generatedRefreshToken");
        when(authUserService.getByLogin(anyString())).thenReturn(Optional.of(user));
        when(jwtProvider.generateAccessToken(user)).thenReturn(expectedJwtResponse.getAccessToken());
        when(jwtProvider.generateRefreshToken(user)).thenReturn(expectedJwtResponse.getRefreshToken());

        // Act
        var jwtResponse = authenticationService.login(request);

        // Assert
        assertEquals(expectedJwtResponse, jwtResponse);
    }

    @Test
    void login_notExistedUser_throwAuthException() throws AuthException {
        // Arrange
        JwtRequest request = new JwtRequest(user.getLogin() + "wrongLogin", user.getPassword());
        when(authUserService.getByLogin(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AuthException.class, () -> authenticationService.login(request));
    }

    @Test
    void login_notExistedPassword_throwAuthException() throws AuthException {
        // Arrange
        JwtRequest request = new JwtRequest(user.getLogin(), user.getPassword() + "wrongPassword");
        when(authUserService.getByLogin(anyString())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(AuthException.class, () -> authenticationService.login(request));
    }

    @Test
    void getAccessToken_nullRequest_nullPointerException() {
        assertThrows(NullPointerException.class, () -> authenticationService.getAccessToken(null));
    }

    @Test
    void getAccessToken_validatedToken_jwtResponse() throws AuthException {
        // Arrange
        String request = "refreshToken";
        var expectedJwtResponse = new JwtResponse("generatedAccessToken", null);
        var claims = mock(Claims.class);
        when(jwtProvider.validateRefreshToken(anyString())).thenReturn(true);
        when(jwtProvider.getRefreshClaims(anyString())).thenReturn(claims);
        when(claims.getSubject()).thenReturn(user.getLogin());
        when(refreshStorage.get(anyString())).thenReturn(request);
        when(authUserService.getByLogin(anyString())).thenReturn(Optional.of(user));
        when(jwtProvider.generateAccessToken(user)).thenReturn(expectedJwtResponse.getAccessToken());

        // Act
        var jwtResponse = authenticationService.getAccessToken(request);

        // Assert
        assertEquals(expectedJwtResponse, jwtResponse);
    }

    @ParameterizedTest
    @MethodSource("invalidateSavedRefreshToken")
    void getAccessToken_invalidatedSavedToken_jwtResponse(String savedToken) throws AuthException {
        // Arrange
        String request = "refreshToken";
        var expectedJwtResponse = new JwtResponse(null, null);
        var claims = mock(Claims.class);
        when(jwtProvider.validateRefreshToken(anyString())).thenReturn(true);
        when(jwtProvider.getRefreshClaims(anyString())).thenReturn(claims);
        when(claims.getSubject()).thenReturn(user.getLogin());
        when(refreshStorage.get(anyString())).thenReturn(savedToken);

        // Act
        var jwtResponse = authenticationService.getAccessToken(request);

        // Assert
        assertEquals(expectedJwtResponse, jwtResponse);
    }

    @Test
    void getAccessToken_notExistedUser_throwAuthException() throws AuthException {
        // Arrange
        String request = "refreshToken";
        var claims = mock(Claims.class);
        when(jwtProvider.validateRefreshToken(anyString())).thenReturn(true);
        when(jwtProvider.getRefreshClaims(anyString())).thenReturn(claims);
        when(claims.getSubject()).thenReturn(user.getLogin());
        when(refreshStorage.get(anyString())).thenReturn(request);
        when(authUserService.getByLogin(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AuthException.class, () -> authenticationService.getAccessToken(request));
    }

    @Test
    void getAccessToken_invalidatedRefreshToken_jwtResponse() throws AuthException {
        // Arrange
        String request = "refreshToken";
        var expectedJwtResponse = new JwtResponse(null, null);
        when(jwtProvider.validateRefreshToken(anyString())).thenReturn(false);

        // Act
        var jwtResponse = authenticationService.getAccessToken(request);

        // Assert
        assertEquals(expectedJwtResponse, jwtResponse);
    }

    @Test
    void refresh_nullRequest_nullPointerException() {
        assertThrows(NullPointerException.class, () -> authenticationService.refresh(null));
    }

    @Test
    void refresh_validatedToken_jwtResponse() throws AuthException {
        // Arrange
        String request = "refreshToken";
        var expectedJwtResponse = new JwtResponse("generatedAccessToken", "newGeneratedRefreshToken");
        var claims = mock(Claims.class);
        when(jwtProvider.validateRefreshToken(anyString())).thenReturn(true);
        when(jwtProvider.getRefreshClaims(anyString())).thenReturn(claims);
        when(claims.getSubject()).thenReturn(user.getLogin());
        when(refreshStorage.get(anyString())).thenReturn(request);
        when(authUserService.getByLogin(anyString())).thenReturn(Optional.of(user));
        when(jwtProvider.generateAccessToken(user)).thenReturn(expectedJwtResponse.getAccessToken());
        when(jwtProvider.generateRefreshToken(user)).thenReturn(expectedJwtResponse.getRefreshToken());


        // Act
        var jwtResponse = authenticationService.refresh(request);

        // Assert
        assertEquals(expectedJwtResponse, jwtResponse);
    }

    @ParameterizedTest
    @MethodSource("invalidateSavedRefreshToken")
    void refresh_invalidatedSavedToken_throwAuthException(String savedToken) throws AuthException {
        // Arrange
        String request = "refreshToken";
        var claims = mock(Claims.class);
        when(jwtProvider.validateRefreshToken(anyString())).thenReturn(true);
        when(jwtProvider.getRefreshClaims(anyString())).thenReturn(claims);
        when(claims.getSubject()).thenReturn(user.getLogin());
        when(refreshStorage.get(anyString())).thenReturn(savedToken);

        // Act & Assert
        assertThrows(AuthException.class, () -> authenticationService.refresh(request));
    }

    @Test
    void refresh_notExistedUser_jwtResponse() throws AuthException {
        // Arrange
        String request = "refreshToken";
        var claims = mock(Claims.class);
        when(jwtProvider.validateRefreshToken(anyString())).thenReturn(true);
        when(jwtProvider.getRefreshClaims(anyString())).thenReturn(claims);
        when(claims.getSubject()).thenReturn(user.getLogin());
        when(refreshStorage.get(anyString())).thenReturn(request);
        when(authUserService.getByLogin(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AuthException.class, () -> authenticationService.refresh(request));
    }

    @Test
    void refresh_invalidatedRefreshToken_throwAuthException() throws AuthException {
        // Arrange
        String request = "refreshToken";
        when(jwtProvider.validateRefreshToken(anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(AuthException.class, () -> authenticationService.refresh(request));
    }
}