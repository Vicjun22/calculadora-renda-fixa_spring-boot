package com.calculadora.renda.fixa.di.util;

import com.calculadora.renda.fixa.di.exception.InvalidParameterException;
import com.calculadora.renda.fixa.di.exception.NotFoundParameterException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.calculadora.renda.fixa.di.util.RegrasUtil.validateParameter;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RegrasUtilTest {

    @Test
    void testParameterIsBadRequest() {
        String errorMessage = "Bad request!";
        assertThrows(InvalidParameterException.class, () -> RegrasUtil.parameterIsBadRequest(errorMessage));
    }

    @Test
    void testParameterIsNotFound() {
        String errorMessage = "Not Found!";
        assertThrows(NotFoundParameterException.class, () -> RegrasUtil.parameterNotFound(errorMessage));
    }

    @Test
    void testValidateParameter() {
        String errorMessage = "Error";
        assertThrows(InvalidParameterException.class, () -> validateParameter(true, errorMessage));
    }

    @Test
    void testDoesNotThrowWhenValidateParameter() {
        String errorMessage = "Error";
        assertDoesNotThrow(() -> validateParameter(false, errorMessage));
    }
}
