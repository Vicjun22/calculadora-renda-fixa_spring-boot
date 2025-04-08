package com.calculadora.renda.fixa.di.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

public class CalcularFator {

    private CalcularFator() { }

    public static BigDecimal calcularFator(List<BigDecimal> fatorDiarioList, BigDecimal percentual) {
        BigDecimal fatorDiarioAtual = fatorDiarioList.get(0);

        fatorDiarioList.remove(0);
        BigDecimal fatorCalculado = BigDecimal.ONE;

        for (BigDecimal fatorDiario : fatorDiarioList) {
            if (Objects.isNull(fatorDiario)) throw new RuntimeException("Ocorreu um erro ::: Valor do Indexador não encontrado.");

            fatorDiarioAtual = fatorDiario.equals(fatorDiarioAtual) ? fatorDiario : fatorDiarioAtual;

            BigDecimal fatorDiaAjustado = ajusteFatorDia(fatorDiarioAtual, percentual);
            fatorCalculado = fatorDiaAjustado.multiply(fatorCalculado).setScale(12, RoundingMode.FLOOR);

            fatorDiarioAtual = fatorDiario;
        }

        return fatorCalculado.setScale(8, RoundingMode.HALF_UP);
    }

    public static BigDecimal ajusteFatorDia(BigDecimal fatorDia, BigDecimal percentual) {
        // (fatorDia - 1) * percentual + 1
        BigDecimal fatorDiaMenosUm = fatorDia.subtract(BigDecimal.ONE);
        BigDecimal percentualDecimal = percentual.divide(BigDecimal.valueOf(100), 12, RoundingMode.FLOOR);
        return fatorDiaMenosUm.multiply(percentualDecimal).add(BigDecimal.ONE).setScale(12, RoundingMode.FLOOR);
    }
}
