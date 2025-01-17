package org.example.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "accounts")
public class Account {
    @Id
    @NotBlank(message = "Id should not be blank")
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    @Basic
    private Instant openedDate;

    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    @Basic
    private Instant closedDate;

    @Min(value = 0, message = "Type should be non-negative")
    @Max(value = 4, message = "Type should be less or equal 4")
    private int type;

    @NotBlank(message = "Name should not be blank")
    private String name;

    @Min(value = 0, message = "Status should be non-negative")
    @Max(value = 4, message = "Status should be less or equal 4")
    private int status;

    @Min(value = 0, message = "Access level should be non-negative")
    @Max(value = 3, message = "Access level should be less or equal 3")
    private int accessLevel;
}
