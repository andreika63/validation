package com.kay.validation.validation;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationResultTest {

    private final ValidationResult ok = ValidationResult.ok();
    private final ValidationResult error1 = ValidationResult.error("error1");
    private final ValidationResult error2 = ValidationResult.error("error2");

    @Test
    public void ok() {
        assertTrue(ok.isValid());
        assertNull(ok.getError());
        assertTrue(ok.getErrorList().isEmpty());
    }

    @Test
    public void error() {
        assertFalse(error1.isValid());
        assertEquals("[error1]", error1.getError());
        assertEquals(List.of("error1"), error1.getErrorList());
    }

    @Test
    public void of() {
        ValidationResult empty = ValidationResult.of();
        assertTrue(empty.isValid());
        assertNull(empty.getError());
        assertTrue(empty.getErrorList().isEmpty());

        ValidationResult of = ValidationResult.of(error1, empty, ok, error2);
        assertFalse(of.isValid());
        assertEquals("[error1]\n[error2]", of.getError());
        assertEquals(List.of("error1", "error2"), of.getErrorList());

        ValidationResult allValid = ValidationResult.of(ok, empty, empty, ok);
        assertTrue(allValid.isValid());
        assertNull(allValid.getError());
        assertTrue(allValid.getErrorList().isEmpty());
    }
}
