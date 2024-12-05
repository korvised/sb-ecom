package com.ecommerce.exceptions;

import lombok.NoArgsConstructor;

import java.io.Serial;

@NoArgsConstructor
public class ApiException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ApiException(String message) {
        super(message);
    }
}
