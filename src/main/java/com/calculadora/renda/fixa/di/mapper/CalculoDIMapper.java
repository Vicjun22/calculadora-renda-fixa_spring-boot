package com.calculadora.renda.fixa.di.mapper;

import com.calculadora.renda.fixa.di.model.CalculoDIResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CalculoDIMapper {

    private CalculoDIMapper() { }

    public static CalculoDIResponse toResponse(LocalDate dataInicial, LocalDate dataFinal, BigDecimal percentual,
                                               BigDecimal fator, BigDecimal taxa, BigDecimal valorBase, BigDecimal valorCalculado) {

        CalculoDIResponse response = new CalculoDIResponse();
        response.setIndicador("DI");
        response.setDataInicial(dataInicial.toString());
        response.setDataFinal(dataFinal.toString());
        response.setPercentual(percentual.toString().concat("%"));
        response.setFator(fator);
        response.setTaxa(taxa.toString().concat("%"));
        response.setValorBase(valorBase);
        response.setValorCalculado(valorCalculado);

        return response;
    }
}
