package com.calculadora.renda.fixa.di.mock;

import com.calculadora.renda.fixa.di.model.CalculoDIResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CalculoDIMock {

    private CalculoDIMock() { }

    public static CalculoDIResponse toResponse() {
        CalculoDIResponse response = new CalculoDIResponse();
        response.setIndicador("DI");
        response.setDataInicial(LocalDate.of(2024, 1, 1).toString());
        response.setDataFinal(LocalDate.of(2024, 6, 30).toString());
        response.setPercentual(new BigDecimal("110").toString());
        response.setFator(new BigDecimal("1.05752841"));
        response.setTaxa(new BigDecimal("5.75").toString());
        response.setValorBase(new BigDecimal("1000"));
        response.setValorCalculado(new BigDecimal("1057.52"));

        return response;
    }
}
