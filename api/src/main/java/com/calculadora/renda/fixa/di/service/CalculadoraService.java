package com.calculadora.renda.fixa.di.service;

import com.calculadora.renda.fixa.di.domain.model.FeriadoModel;
import com.calculadora.renda.fixa.di.domain.model.TaxaSelicModel;
import com.calculadora.renda.fixa.di.model.CalculoDIResponse;
import com.calculadora.renda.fixa.di.util.ConsultarFeriados;
import com.calculadora.renda.fixa.di.util.ConsultarTaxaSelic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.calculadora.renda.fixa.di.mapper.CalculoDIMapper.toResponse;
import static com.calculadora.renda.fixa.di.util.CalcularFator.calcularFator;
import static com.calculadora.renda.fixa.di.util.CalendarioUtil.ajustarParaDiaUtil;
import static com.calculadora.renda.fixa.di.util.RegrasUtil.validateParameter;

@Service
@RequiredArgsConstructor
public class CalculadoraService {

    private final ConsultarTaxaSelic consultarTaxaSelic;
    private final ConsultarFeriados consultarFeriados;

    public CalculoDIResponse calculoFatorDi(LocalDate dataInicial, LocalDate dataFinal, BigDecimal percentual, BigDecimal valor) {

        // Principais regras
        validateParameter(dataInicial.isAfter(LocalDate.now()), "A data inicial não pode ser posterior ao dia atual");
        validateParameter(dataFinal.isAfter(LocalDate.now()), "A data final não pode ser posterior ao dia atual");

        // Importante saber se a data final cai em um final de semana ou em um feriado.
        List<FeriadoModel> feriados = Objects.requireNonNull(consultarFeriados.buscarListaDeFeriados(String.valueOf(dataFinal.getYear())));
        List<FeriadoModel> feriadosNacionais = feriados.stream().filter(feriado -> feriado.getType().equals("national")).toList();
        LocalDate dataFinalEmDiaUtil = ajustarParaDiaUtil(dataFinal, feriadosNacionais);

        // Realiza a busca das taxas SELIC no período de tempo
        List<TaxaSelicModel> taxaSelic = Objects.requireNonNull(consultarTaxaSelic.buscarListaDasTaxasSelic(dataInicial, dataFinalEmDiaUtil));

        // Se a data final for o dia atual, então a taxa do dia é o valor da taxa de fechamento do dia útil anterior
        boolean isHojeDataFinal = dataFinal.equals(LocalDate.now());
        boolean hasUltimoDia = taxaSelic.stream().anyMatch(txSelic -> txSelic.getData().equals(dataFinalEmDiaUtil.toString()));
        taxaSelic = isHojeDataFinal && !hasUltimoDia ? updateTaxaSelicList(taxaSelic) : taxaSelic;

        // Realiza os cálculos de fator diário
        List<BigDecimal> fatorDiario = defineFatorDiario(taxaSelic);

        // Descobre o preço unitário
        BigDecimal fatorComPercentualAplicado = calcularFator(fatorDiario, percentual);

        // Descobre o valor calculado
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

    private List<TaxaSelicModel> updateTaxaSelicList(List<TaxaSelicModel> taxaSelicList) {
        String valor = taxaSelicList.get(taxaSelicList.size() - 1).getValor();
        TaxaSelicModel taxaSelicDiaAtual = new TaxaSelicModel(LocalDate.now().toString(), valor);

        List<TaxaSelicModel> listaAtualizada = new ArrayList<>(taxaSelicList);
        listaAtualizada.add(taxaSelicDiaAtual);
        return listaAtualizada;
    }
}
