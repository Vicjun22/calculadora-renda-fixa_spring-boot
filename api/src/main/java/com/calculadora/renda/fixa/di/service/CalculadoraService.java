package com.calculadora.renda.fixa.di.service;

import com.calculadora.renda.fixa.di.domain.model.FeriadoModel;
import com.calculadora.renda.fixa.di.domain.model.TaxaSelicModel;
import com.calculadora.renda.fixa.di.model.CalculoDIResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.calculadora.renda.fixa.di.mapper.CalculoDIMapper.toResponse;
import static com.calculadora.renda.fixa.di.util.CalcularFator.calcularFator;
import static com.calculadora.renda.fixa.di.util.CalendarioUtil.ajustarParaDiaUtil;
import static com.calculadora.renda.fixa.di.util.ConsultarFeriados.buscarListaDeFeriados;
import static com.calculadora.renda.fixa.di.util.ConsultarTaxaSelic.buscarListaDasTaxasSelic;

@Service
@RequiredArgsConstructor
public class CalculadoraService {

    public CalculoDIResponse calculoFatorDi(LocalDate dataInicial, LocalDate dataFinal, BigDecimal percentual, BigDecimal valor) {

        List<FeriadoModel> feriados = buscarListaDeFeriados(String.valueOf(dataFinal.getYear()));
        List<FeriadoModel> feriadosNacionais = feriados.stream().filter(feriado -> feriado.getType().equals("national")).toList();
        LocalDate dataFinalEmDiaUtil = ajustarParaDiaUtil(dataFinal, feriadosNacionais);

        List<TaxaSelicModel> taxaSelic = buscarListaDasTaxasSelic(dataInicial, dataFinalEmDiaUtil);
        List<BigDecimal> fatorDiario = defineFatorDiario(taxaSelic);
        BigDecimal fatorComPercentualAplicado = calcularFator(fatorDiario, percentual);

        BigDecimal valorCalculado = fatorComPercentualAplicado.multiply(valor).setScale(2, RoundingMode.FLOOR);

        BigDecimal taxaDeAumento = ((valorCalculado.subtract(valor))
                .divide(valor, MathContext.DECIMAL64))
                .multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_EVEN);

        return toResponse(dataInicial, dataFinal, percentual, fatorComPercentualAplicado, taxaDeAumento, valor, valorCalculado);
    }

    private List<BigDecimal> defineFatorDiario(List<TaxaSelicModel> taxaSelic) {
        List<BigDecimal> fatorDiario = new ArrayList<>();

        for (TaxaSelicModel taxaSelicModel : taxaSelic) {
            fatorDiario.add(new BigDecimal(taxaSelicModel.getValor())
                    .divide(new BigDecimal("100"), MathContext.DECIMAL64)
                    .add(BigDecimal.ONE));
        }
        return fatorDiario;
    }
}
