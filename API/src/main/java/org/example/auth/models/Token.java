package org.example.auth.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Token {
    @Id
    private int id;
    private int userId;
    private String token;
}
