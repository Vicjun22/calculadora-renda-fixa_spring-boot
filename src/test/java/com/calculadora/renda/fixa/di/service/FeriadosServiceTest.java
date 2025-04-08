package com.calculadora.renda.fixa.di.service;

import com.calculadora.renda.fixa.di.domain.model.FeriadoModel;
import com.calculadora.renda.fixa.di.repository.FeriadosRepository;
import com.calculadora.renda.fixa.di.util.ConsultarFeriados;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.calculadora.renda.fixa.di.mock.FeriadoModelMock.listaFeriadosMock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeriadosServiceTest {

    @InjectMocks
    private FeriadosService service;

    @Mock
    private FeriadosRepository repository;

    @Mock
    private ConsultarFeriados consultarFeriados;

    @Test
    @DisplayName("Não deve inserir dados na tabela quando a listagem de feriados do ano consultado retornar uma lista")
    void naoDeveInserirDadosNaTabelaQuandoJaHouverDadosDoAnoConsultado() {
        when(repository.listarFeriadosPorAno(any())).thenReturn(List.of(mock(FeriadoModel.class)));

        service.inserirFeriadosReferentesAoAno("2025");

        verify(repository, times(1)).listarFeriadosPorAno("2025");
        verify(consultarFeriados, never()).buscarListaDeFeriados(anyString());
        verify(repository, never()).inserirFeriadosEmLote(any(), anyString());
    }

    @Test
    @DisplayName("Deve inserir dados na tabela quando a listagem de feriados do ano consultado retornar vazio")
    void deveInserirDadosNaTabelaQuandoFeriadosDoAnoConsultadoNaoEstiveremSendoListados() {
        when(repository.listarFeriadosPorAno(any())).thenReturn(List.of());
        when(consultarFeriados.buscarListaDeFeriados("2024")).thenReturn(listaFeriadosMock());

        service.inserirFeriadosReferentesAoAno("2024");

        verify(repository, times(1)).listarFeriadosPorAno("2024");
        verify(consultarFeriados, times(1)).buscarListaDeFeriados("2024");
        verify(repository, times(1)).inserirFeriadosEmLote(listaFeriadosMock(), "2024");

        assertEquals(13, consultarFeriados.buscarListaDeFeriados("2024").size());
    }
}
