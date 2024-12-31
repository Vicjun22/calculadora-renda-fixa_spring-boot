package com.calculadora.renda.fixa.di.util;

import com.calculadora.renda.fixa.di.domain.model.FeriadoModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConsultarFeriadosTest {

    @InjectMocks
    private ConsultarFeriados consultarFeriados;

    @Mock
    private RestTemplate restTemplate;

    private static final String BRASIL_API_URL = "https://brasilapi.com.br/api";

    @Test
    @DisplayName("Deve retornar lista de feriados com sucesso")
    void buscarListaDeFeriados_sucesso() {
        String ano = "2024";
        FeriadoModel[] feriados = {
                new FeriadoModel(LocalDate.of(2024, 1, 1), "Ano Novo", "national"),
                new FeriadoModel(LocalDate.of(2024, 4, 21), "Tiradentes", "national")
        };

        String url = BRASIL_API_URL.concat("/feriados/v1/").concat(ano);
        when(restTemplate.getForEntity(eq(url), eq(FeriadoModel[].class))).thenReturn(ResponseEntity.ok(feriados));

        List<FeriadoModel> result = consultarFeriados.buscarListaDeFeriados(ano);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ano Novo", result.get(0).getName());
        assertEquals("Tiradentes", result.get(1).getName());
    }

    @Test
    @DisplayName("Deve lançar exceção quando a API retorna corpo nulo")
    void buscarListaDeFeriados_corpoNulo() {
        String ano = "2024";

        String url = BRASIL_API_URL.concat("/feriados/v1/").concat(ano);
        when(restTemplate.getForEntity(eq(url), eq(FeriadoModel[].class))).thenReturn(ResponseEntity.ok(null));

        Exception exception = assertThrows(RuntimeException.class, () -> consultarFeriados.buscarListaDeFeriados(ano));
        assertEquals("Ocorreu um erro! Nenhum feriado encontrado.", exception.getMessage());
        verify(restTemplate, times(1)).getForEntity(url, FeriadoModel[].class);
    }

    @Test
    @DisplayName("Deve lançar exceção quando ocorre um erro durante a consulta")
    void buscarListaDeFeriados_erroConsulta() {
        String ano = "2024";
        String url = BRASIL_API_URL.concat("/feriados/v1/").concat(ano);

        when(restTemplate.getForEntity(eq(url), eq(FeriadoModel[].class))).thenThrow(new RuntimeException("Erro na consulta"));

        Exception exception = assertThrows(RuntimeException.class, () -> consultarFeriados.buscarListaDeFeriados(ano));
        assertEquals("Ocorreu um erro ao consultar a lista de feriados.", exception.getMessage());
        verify(restTemplate, times(1)).getForEntity(url, FeriadoModel[].class);
    }
}
