package com.calculadora.renda.fixa.di.service;

import com.calculadora.renda.fixa.di.model.CalculoDIResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CalculadoraServiceTest {

    @InjectMocks
    private CalculadoraService service;

    @Test
    @DisplayName("Deve realizar o teste do cálculo do fator de DI")
    void calculoFatorDiTest() {
        LocalDate dataInicial = LocalDate.of(2024, 1, 1);
        LocalDate dataFinal = LocalDate.of(2024, 12, 1);
        BigDecimal percentual = BigDecimal.TEN;
        BigDecimal valor = BigDecimal.TEN;

        CalculoDIResponse response = service.calculoFatorDi(dataInicial, dataFinal, percentual, valor);

        assertEquals("2024-01-01", response.getDataInicial());
        assertEquals("2024-12-01", response.getDataFinal());
        assertEquals("10%", response.getPercentual());
        assertEquals(new BigDecimal("1.00755266"), response.getFator());
        assertEquals("0.70%", response.getTaxa());
        assertEquals(new BigDecimal("10"), response.getValorBase());
        assertEquals(new BigDecimal("10.07"), response.getValorCalculado());
    }
}
