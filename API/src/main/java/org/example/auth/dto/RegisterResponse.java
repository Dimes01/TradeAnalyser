package org.example.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    @JsonProperty("username")
    private String username;

    @JsonProperty("success_register")
    private boolean successRegister;

    @JsonProperty("success_get_accounts")
    private boolean successGetAccounts;
}
