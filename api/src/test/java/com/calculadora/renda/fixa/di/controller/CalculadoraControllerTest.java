package com.calculadora.renda.fixa.di.controller;

import com.calculadora.renda.fixa.di.mock.CalculoDIMock;
import com.calculadora.renda.fixa.di.model.CalculoDIResponse;
import com.calculadora.renda.fixa.di.service.CalculadoraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CalculadoraControllerTest {

    @InjectMocks
    private CalculadoraController controller;

    @Mock
    private CalculadoraService service;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    @DisplayName("Deve testar a chamada do calculo do fator de DI na controller")
    void calculoFatorDiTest() throws Exception {
       LocalDate dataInicial = LocalDate.of(2024, 1, 1);
       LocalDate dataFinal = LocalDate.of(2024, 12, 1);
       BigDecimal percentual = BigDecimal.TEN;
       BigDecimal valor = BigDecimal.TEN;

       when(service.calculoFatorDi(dataInicial, dataFinal, percentual, valor)).thenReturn(CalculoDIMock.toResponse());

        mockMvc.perform(get("/calcula-fator-di")
                        .param("dataInicial", dataInicial.toString())
                        .param("dataFinal", dataFinal.toString())
                        .param("percentual", percentual.toString())
                        .param("valor", valor.toString()))
                .andExpect(status().isOk());

        verifyNoMoreInteractions(service);

        ResponseEntity<CalculoDIResponse> responseEntity = controller.calculoFatorDi(dataInicial, dataFinal, percentual, valor);
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);
    }
}
