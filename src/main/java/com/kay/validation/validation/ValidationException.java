package com.kay.validation.validation;

import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {
    private final ValidationResult validationResult;
    public ValidationException(String message, ValidationResult validationResult) {
        super(message);
        this.validationResult = validationResult;
    }

    @Override
    public String getMessage() {
        return "%s: %s".formatted(
                super.getMessage(),
                validationResult.getErrorList().stream()
                        .map("[%s]"::formatted)
                        .collect(Collectors.joining(", "))
        );
    }
}
