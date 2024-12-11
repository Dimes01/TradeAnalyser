package org.example.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
