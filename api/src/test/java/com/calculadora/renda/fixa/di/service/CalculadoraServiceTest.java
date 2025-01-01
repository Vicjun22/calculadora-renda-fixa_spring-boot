package com.calculadora.renda.fixa.di.service;

import com.calculadora.renda.fixa.di.domain.model.FeriadoModel;
import com.calculadora.renda.fixa.di.domain.model.TaxaSelicModel;
import com.calculadora.renda.fixa.di.model.CalculoDIResponse;
import com.calculadora.renda.fixa.di.util.ConsultarFeriados;
import com.calculadora.renda.fixa.di.util.ConsultarTaxaSelic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculadoraServiceTest {

    @InjectMocks
    private CalculadoraService service;

    @Mock
    private ConsultarTaxaSelic consultarTaxaSelic;

    @Mock
    private ConsultarFeriados consultarFeriados;

    @Test
    @DisplayName("Deve realizar o cálculo do fator de DI com mocks de APIs externas")
    void calculoFatorDiTestComMocks() {

        LocalDate dataInicial = LocalDate.of(2024, 1, 1);
        LocalDate dataFinal = LocalDate.of(2024, 1, 5);
        BigDecimal percentual = new BigDecimal("100");
        BigDecimal valor = new BigDecimal("1000");

        List<FeriadoModel> feriados = List.of(
                new FeriadoModel(LocalDate.of(2024, 1, 1), "Ano Novo", "national")
        );
        when(consultarFeriados.buscarListaDeFeriados("2024")).thenReturn(feriados);

        List<TaxaSelicModel> taxaSelic = List.of(
                new TaxaSelicModel("2024-01-02", "0.043739"),
                new TaxaSelicModel("2024-01-03", "0.043739"),
                new TaxaSelicModel("2024-01-04", "0.043739"),
                new TaxaSelicModel("2024-01-05", "0.043739")
        );
        when(consultarTaxaSelic.buscarListaDasTaxasSelic(dataInicial, dataFinal)).thenReturn(taxaSelic);

        CalculoDIResponse response = service.calculoFatorDi(dataInicial, dataFinal, percentual, valor);

        assertEquals("2024-01-01", response.getDataInicial());
        assertEquals("2024-01-05", response.getDataFinal());
        assertEquals("100%", response.getPercentual());
        assertEquals(new BigDecimal("1.00131274"), response.getFator());
        assertEquals("0.13%", response.getTaxa());
        assertEquals(new BigDecimal("1000"), response.getValorBase());
        assertEquals(new BigDecimal("1001.31"), response.getValorCalculado());
    }

    @Test
    @DisplayName("Deve adicionar a taxa do dia atual à lista de taxas existentes")
    void calculoFatorDiTestComDiaAtualUtilizandoMocks() {
        LocalDate dataInicial = LocalDate.now().minusDays(1L);
        LocalDate dataFinal = LocalDate.now();
        BigDecimal percentual = new BigDecimal("100");
        BigDecimal valor = new BigDecimal("1000");

        when(consultarFeriados.buscarListaDeFeriados(String.valueOf(LocalDate.now().getYear()))).thenReturn(List.of());

        List<TaxaSelicModel> taxaSelic = List.of(new TaxaSelicModel(LocalDate.now().minusDays(1L).toString(), "0.043739"));

        when(consultarTaxaSelic.buscarListaDasTaxasSelic(dataInicial, dataFinal)).thenReturn(taxaSelic);

        CalculoDIResponse response = service.calculoFatorDi(dataInicial, dataFinal, percentual, valor);

        assertEquals(LocalDate.now().minusDays(1L).toString(), response.getDataInicial());
        assertEquals(LocalDate.now().toString(), response.getDataFinal());
        assertEquals("100%", response.getPercentual());
        assertEquals(new BigDecimal("1.00043739"), response.getFator());
        assertEquals("0.04%", response.getTaxa());
        assertEquals(new BigDecimal("1000"), response.getValorBase());
        assertEquals(new BigDecimal("1000.43"), response.getValorCalculado());
    }
}
