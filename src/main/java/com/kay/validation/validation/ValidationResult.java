package com.kay.validation.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ValidationResult {

    private final List<String> errorList = new ArrayList<>();

    public static ValidationResult ok() {
        return new ValidationResult();
    }
    public static ValidationResult error(String... errors) {
        return new ValidationResult(errors);
    }
    public static ValidationResult of(ValidationResult... results) {
        return new ValidationResult(Stream.of(results).flatMap(r -> r.errorList.stream()).toArray(String[]::new));
    }

    private ValidationResult() {
    }

    private ValidationResult(String... error) {
        errorList.addAll(List.of(error));
    }

    public boolean isValid() {
        return errorList.isEmpty();
    }

    public String getError() {
        if (isValid()) {
            return null;
        }
        return errorList.stream()
                .map("[%s]"::formatted)
                .collect(Collectors.joining("\n"));
    }

    public List<String> getErrorList() {
        return List.copyOf(errorList);
    }

    public void andThrow(Function<ValidationResult, String> message) {
        if (!this.isValid()) {
            throw new ValidationException(message.apply(this), this);
        }
    }
}
