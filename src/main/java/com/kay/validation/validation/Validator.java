package com.kay.validation.validation;

import java.util.function.Supplier;
import java.util.stream.Stream;

public interface Validator {

    ValidationResult validate();

    static Validator of(String error, Supplier<Boolean> isError) {
        return () -> tryValidate(() -> isError.get() ? ValidationResult.error(error) : ValidationResult.ok());
    }

    static Validator of(Runnable runnable) {
        return of("", () -> {
            runnable.run();
            return false;
        });
    }

    static Validator of(Validator... validators) {
        return Stream.of(validators)
                .reduce(Validator::and)
                .orElseGet(() -> ValidationResult::ok);
    }

    private static ValidationResult tryValidate(Supplier<ValidationResult> block) {
        try {
            return block.get();
        } catch (Throwable th) {
            String message = th.getMessage();
            return ValidationResult.error(message != null ? message : th.toString());
        }
    }

    default Validator and(Validator validator) {
        return () -> ValidationResult.of(tryValidate(this::validate), tryValidate(validator::validate));
    }

}
