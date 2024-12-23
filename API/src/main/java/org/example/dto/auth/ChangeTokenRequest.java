package org.example.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NotBlank
@AllArgsConstructor
public class ChangeTokenRequest {
    private String token;
}
