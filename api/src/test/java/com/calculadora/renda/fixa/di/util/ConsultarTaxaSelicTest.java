package com.calculadora.renda.fixa.di.util;

import com.calculadora.renda.fixa.di.domain.model.TaxaSelicModel;
import com.calculadora.renda.fixa.di.exception.NotFoundParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsultarTaxaSelicTest {

    @InjectMocks
    private ConsultarTaxaSelic consultarTaxaSelic;

    @Mock
    private RestTemplate restTemplate;

    private static final String BANCO_CENTRAL_DO_BRASIL_CONSULTA_SELIC_URL = "https://api.bcb.gov.br/dados/serie/bcdata.sgs.11/dados?formato=json";

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Test
    @DisplayName("Deve retornar lista de taxas SELIC válida")
    void buscarListaDasTaxasSelic_respostaValida() {
        LocalDate dataInicio = LocalDate.of(2024, 1, 1);
        LocalDate dataFim = LocalDate.of(2024, 1, 31);
        String url = BANCO_CENTRAL_DO_BRASIL_CONSULTA_SELIC_URL +
                "&dataInicial=" + dataInicio.format(FORMATTER) +
                "&dataFinal=" + dataFim.format(FORMATTER);

        TaxaSelicModel[] taxaSelicModels = {new TaxaSelicModel("01/01/2024", "13.65")};
        when(restTemplate.getForEntity(eq(url), eq(TaxaSelicModel[].class))).thenReturn(ResponseEntity.ok(taxaSelicModels));

        List<TaxaSelicModel> result = consultarTaxaSelic.buscarListaDasTaxasSelic(dataInicio, dataFim);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("13.65", result.get(0).getValor());
        verify(restTemplate, times(1)).getForEntity(url, TaxaSelicModel[].class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a API retorna corpo nulo")
    void buscarListaDasTaxasSelic_corpoNulo() {
        LocalDate dataInicio = LocalDate.of(2024, 1, 1);
        LocalDate dataFim = LocalDate.of(2024, 1, 31);
        String url = BANCO_CENTRAL_DO_BRASIL_CONSULTA_SELIC_URL +
                "&dataInicial=" + dataInicio.format(FORMATTER) +
                "&dataFinal=" + dataFim.format(FORMATTER);

        when(restTemplate.getForEntity(eq(url), eq(TaxaSelicModel[].class))).thenReturn(ResponseEntity.ok(null));

        Exception exception = assertThrows(NotFoundParameterException.class, () -> consultarTaxaSelic.buscarListaDasTaxasSelic(dataInicio, dataFim));
        assertEquals("Ocorreu um erro! Nenhuma taxa encontrada.", exception.getMessage());
        verify(restTemplate, times(1)).getForEntity(url, TaxaSelicModel[].class);
    }

    @Test
    @DisplayName("Deve lançar exceção para erro inesperado")
    void buscarListaDasTaxasSelic_erroInesperado() {
        LocalDate dataInicio = LocalDate.of(2024, 1, 1);
        LocalDate dataFim = LocalDate.of(2024, 1, 31);
        String url = BANCO_CENTRAL_DO_BRASIL_CONSULTA_SELIC_URL +
                "&dataInicial=" + dataInicio.format(FORMATTER) +
                "&dataFinal=" + dataFim.format(FORMATTER);

        when(restTemplate.getForEntity(eq(url), eq(TaxaSelicModel[].class))).thenThrow(new RuntimeException("Erro ao acessar API"));

        Exception exception = assertThrows(RuntimeException.class, () -> consultarTaxaSelic.buscarListaDasTaxasSelic(dataInicio, dataFim));
        assertEquals("Ocorreu um erro ao consultar a lista de taxas SELIC.", exception.getMessage());
        verify(restTemplate, times(1)).getForEntity(url, TaxaSelicModel[].class);
    }
}

