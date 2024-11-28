package org.example.auth.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.auth.entities.User;
import org.example.auth.models.Role;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final List<User> users;

    public AuthUserService() {
        this.users = List.of(
            new User("user", "1234", "FirstName", "LastName", Collections.singleton(Role.USER)),
            new User("admin", "12345", "FirstName", "LastName", Collections.singleton(Role.ADMIN))
        );
    }

    public Optional<User> getByLogin(@NonNull String login) {
        return users.stream()
            .filter(user -> login.equals(user.getLogin()))
            .findFirst();
    }

}
