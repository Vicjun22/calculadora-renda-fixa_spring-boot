package com.calculadora.renda.fixa.di.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class DiConfigTest {

    @InjectMocks
    private DiConfig config;

    @Test
    @DisplayName("Deve carregar o bean RestTemplate corretamente")
     void testRestTemplateBean() {
        assertNotNull(config.restTemplate());
    }
}
