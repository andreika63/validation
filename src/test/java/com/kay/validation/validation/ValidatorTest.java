package com.kay.validation.validation;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    private final Validator ok = Validator.of();
    private final Validator noError = Validator.of("some error", () -> false);
    private final Validator error1 = Validator.of("error1", () -> true);
    private final Validator error2 = Validator.of("error2", () -> true);
    private final Validator ex = Validator.of(() -> { throw new RuntimeException("exception"); });

    @Test
    public void of() {
        assertTrue(ok.validate().isValid());
        assertNull(ok.validate().getError());

        assertTrue(noError.validate().isValid());
        assertNull(noError.validate().getError());

        assertFalse(error1.validate().isValid());
        assertEquals("[error1]", error1.validate().getError());

        assertFalse(error2.validate().isValid());
        assertEquals("[error2]", error2.validate().getError());

        assertFalse(ex.validate().isValid());
        assertEquals("[exception]", ex.validate().getError());
    }

    @Test
    public void ofVararg() {
        ValidationResult result = Validator.of(ex, error1, null, noError, error2).validate();
        assertFalse(result.isValid());
        String msg = """
            [exception]
            [error1]
            [error2]""";
        assertEquals(msg, result.getError());
    }

    @Test
    public void and() {
        ValidationResult validationResult = ex.and(error2).and(error1).and(ok).and(noError).and(ex).validate();
        assertFalse(validationResult.isValid());
        assertEquals(List.of("exception", "error2", "error1", "exception"), validationResult.getErrorList());
        assertTrue(ok.and(ok).and(noError).validate().isValid());
    }

    @Test
    public void andThrow() {
        ValidationException ex = assertThrows(ValidationException.class, () -> Validator.of(error1, noError, error2).validate().andThrow(r -> "summary"));
        assertEquals("summary: [error1], [error2]", ex.getMessage());
    }
}
