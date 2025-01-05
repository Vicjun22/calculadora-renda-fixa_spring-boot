package com.calculadora.renda.fixa.di.service;

import com.calculadora.renda.fixa.di.domain.model.TaxaSelicModel;
import com.calculadora.renda.fixa.di.mock.TaxaSelicMock;
import com.calculadora.renda.fixa.di.repository.FeriadosRepository;
import com.calculadora.renda.fixa.di.repository.TaxaSelicRepository;
import com.calculadora.renda.fixa.di.util.ConsultarTaxaSelic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.calculadora.renda.fixa.di.mock.FeriadoModelMock.listaFeriadosMock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaxaSelicServiceTest {

    @InjectMocks
    private TaxaSelicService service;

    @Mock
    private FeriadosRepository feriadosRepository;

    @Mock
    private TaxaSelicRepository taxaSelicRepository;

    @Mock
    private ConsultarTaxaSelic consultarTaxaSelic;

    @Test
    @DisplayName("Deve inserir taxas selic caso ainda não tenha sido armazenado em banco de dados")
    void deveInserirTaxasSelicQuandoNaoArmazenadoEmBancoDeDados() {
        when(taxaSelicRepository.consultarUltimaDataInserida()).thenReturn(null);
        when(feriadosRepository.listarFeriadosPorAno(anyString())).thenReturn(listaFeriadosMock());

        when(consultarTaxaSelic.buscarListaDasTaxasSelic(any(), any())).thenReturn(TaxaSelicMock.listaTaxaSelicMock());

        service.inserirTaxasSelic();

        verify(taxaSelicRepository, times(1)).inserirTaxasEmLote(any());
    }

    @Test
    @DisplayName("Não deve inserir taxas selic caso já tenha sido armazenado em banco de dados")
    void naoDeveInserirTaxasSelicQuandoArmazenadoEmBancoDeDados() {
        when(taxaSelicRepository.consultarUltimaDataInserida())
                .thenReturn(new TaxaSelicModel("2025-01-02", "0.043739"));
        when(feriadosRepository.listarFeriadosPorAno(anyString())).thenReturn(listaFeriadosMock());

        service.inserirTaxasSelic();

        verify(taxaSelicRepository, never()).inserirTaxasEmLote(any());
    }
}
