package org.example.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "Username can not be blank")
    private String username;

    @NotBlank(message = "Password can not be blank")
    private String password;

    @JsonProperty("api_key")
    @NotBlank(message = "Api key can not be blank")
    private String apiKey;
}
