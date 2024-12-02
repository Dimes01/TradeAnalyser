package org.example.data.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    @NotBlank(message = "Id should not be blank")
    private String id;

    @Min(value = 0, message = "Type should be non-negative")
    @Max(value = 4, message = "Type should be less or equal 4")
    private int type;

    @NotBlank(message = "Name should not be blank")
    private String name;

    @Min(value = 0, message = "Status should be non-negative")
    @Max(value = 4, message = "Status should be less or equal 4")
    private int status;

    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant openedDate;

    @JsonSerialize(using = InstantSerializer.class)
    @JsonDeserialize(using = InstantDeserializer.class)
    private Instant closedDate;

    @Min(value = 0, message = "Access level should be non-negative")
    @Max(value = 3, message = "Access level should be less or equal 3")
    private int accessLevel;
}
