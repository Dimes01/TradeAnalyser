package org.example.auth.services;

import org.example.auth.entities.User;
import org.example.auth.models.Role;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthUserServiceTest {
    List<User> users = new ArrayList<>();
    AuthUserService authUserService;

    public AuthUserServiceTest() {
        users.add(new User("user", "1234", "FirstName", "LastName", Collections.singleton(Role.USER)));
        authUserService = new AuthUserService(users);
    }

    @Test
    void getByLogin_existedUser_returnUser() {
        var user = authUserService.getByLogin(users.getFirst().getLogin());
        assertEquals(users.getFirst(), user.orElse(null));
    }

    @Test
    void getByLogin_wrongLogin_throwNPE() {
        assertEquals(Optional.empty(), authUserService.getByLogin("wrongLogin"));
    }

    @Test
    void getByLogin_nullLogin_throwNPE() {
        assertThrows(NullPointerException.class, () -> authUserService.getByLogin(null));
    }
}