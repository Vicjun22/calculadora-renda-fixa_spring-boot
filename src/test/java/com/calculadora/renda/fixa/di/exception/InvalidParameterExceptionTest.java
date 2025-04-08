package com.calculadora.renda.fixa.di.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidParameterExceptionTest {

    @Test
    void testInvalidParameterException() {
        String message = "Ocorreu um erro.";

        InvalidParameterException exception = new InvalidParameterException(message);

        assertEquals(message, exception.getMessage());
    }
}
