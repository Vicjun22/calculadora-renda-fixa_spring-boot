package com.calculadora.renda.fixa.di.mapper;

import com.calculadora.renda.fixa.di.mock.CalculoDIMock;
import com.calculadora.renda.fixa.di.model.CalculoDIResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculoDIMapperTest {

    @Test
    @DisplayName("Deve realizar cobertura de testes para o mapper")
    void toResponseTest() {
        LocalDate dataInicial = LocalDate.parse(CalculoDIMock.toResponse().getDataInicial());
        LocalDate dataFinal = LocalDate.parse(CalculoDIMock.toResponse().getDataFinal());
        BigDecimal percentual = new BigDecimal(CalculoDIMock.toResponse().getPercentual());
        BigDecimal fator = CalculoDIMock.toResponse().getFator();
        BigDecimal taxa = new BigDecimal(CalculoDIMock.toResponse().getTaxa());
        BigDecimal valorBase = CalculoDIMock.toResponse().getValorBase();
        BigDecimal valorCalculado = CalculoDIMock.toResponse().getValorCalculado();

        CalculoDIResponse response = CalculoDIMapper.toResponse(dataInicial, dataFinal, percentual, fator, taxa, valorBase, valorCalculado);

        assertEquals("2024-01-01", response.getDataInicial());
        assertEquals("2024-06-30", response.getDataFinal());
        assertEquals("110%", response.getPercentual());
        assertEquals(new BigDecimal("1.05752841"), response.getFator());
        assertEquals("5.75%", response.getTaxa());
        assertEquals(new BigDecimal("1000"), response.getValorBase());
        assertEquals(new BigDecimal("1057.52"), response.getValorCalculado());
    }
}
