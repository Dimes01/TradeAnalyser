package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshJwtRequest {

    @NotBlank
    public String refreshToken;
}
