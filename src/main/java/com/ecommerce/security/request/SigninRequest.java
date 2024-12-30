package com.ecommerce.security.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SigninRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
