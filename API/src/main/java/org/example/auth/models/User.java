package org.example.auth.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "users")
public class User {
    @Id
    private int id;
    private String username;
    private String password;
}
