package com.calculadora.renda.fixa.di.controller;

import com.calculadora.renda.fixa.di.api.CalculaFatorDiApi;
import com.calculadora.renda.fixa.di.model.CalculoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class CalculadoraController implements CalculaFatorDiApi {

    @Override
    public ResponseEntity<CalculoResponse> calculoFatorDi(LocalDate dataInicial, LocalDate dataFinal, BigDecimal percentual, BigDecimal valor) {
        return null;
    }
}
