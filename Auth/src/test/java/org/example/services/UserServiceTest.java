package org.example.services;

import org.example.entities.User;
import org.example.utilities.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    List<User> users = new ArrayList<>();
    UserService userService;

    public UserServiceTest() {
        users.add(new User("user", "1234", "FirstName", "LastName", Collections.singleton(Role.USER)));
        userService = new UserService(users);
    }

    @Test
    void getByLogin_existedUser_returnUser() {
        var user = userService.getByLogin(users.getFirst().getLogin());
        assertEquals(users.getFirst(), user.orElse(null));
    }

    @Test
    void getByLogin_wrongLogin_throwNPE() {
        assertEquals(Optional.empty(), userService.getByLogin("wrongLogin"));
    }

    @Test
    void getByLogin_nullLogin_throwNPE() {
        assertThrows(NullPointerException.class, () -> userService.getByLogin(null));
    }
}