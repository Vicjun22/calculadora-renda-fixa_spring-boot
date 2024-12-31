package com.calculadora.renda.fixa.di.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    @DisplayName("Should handle InvalidParameterException correctly")
    void shouldHandleInvalidParameterException() {
        InvalidParameterException exception = new InvalidParameterException("Erro de parâmetro inválido");
        ResponseEntity<Object> response = globalExceptionHandler.handlerExceptionResolver(exception);

        assertTrue(response.getBody() instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro de parâmetro inválido", responseBody.get("erro"));
    }

    @Test
    @DisplayName("Should handle NotFoundParameterException correctly")
    void shouldHandleNotFoundParameterException() {
        NotFoundParameterException exception = new NotFoundParameterException("Erro de parâmetro não encontrado");
        ResponseEntity<Object> response = globalExceptionHandler.handlerExceptionResolver(exception);

        assertTrue(response.getBody() instanceof Map);
        @SuppressWarnings("unchecked")
        Map<String, String> responseBody = (Map<String, String>) response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Erro de parâmetro não encontrado", responseBody.get("erro"));
    }

}
