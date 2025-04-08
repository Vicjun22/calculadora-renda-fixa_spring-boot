package com.calculadora.renda.fixa.di.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotFoundParameterExceptionTest {

    @Test
    void testNotFoundParameterException() {
        String message = "Ocorreu um erro.";

        NotFoundParameterException exception = new NotFoundParameterException(message);

        assertEquals(message, exception.getMessage());
    }
}
