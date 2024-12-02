package org.example.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.auth.models.User;

import java.time.Instant;

@Getter @Setter
@Entity
public class Account {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Basic
    private Instant openedDate;

    @Basic
    private Instant closedDate;

    private int type;
    private String name;
    private int status;
    private int accessLevel;
}
