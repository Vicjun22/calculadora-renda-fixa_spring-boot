package com.calculadora.renda.fixa.di.controller;

import com.calculadora.renda.fixa.di.api.CalculaFatorDiApi;
import com.calculadora.renda.fixa.di.model.CalculoDIResponse;
import com.calculadora.renda.fixa.di.service.CalculadoraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class CalculadoraController implements CalculaFatorDiApi {

    private final CalculadoraService service;

    @Override
    public ResponseEntity<CalculoDIResponse> calculoFatorDi(LocalDate dataInicial, LocalDate dataFinal, BigDecimal percentual, BigDecimal valor) {

        return ResponseEntity.ok(service.calculoFatorDi(dataInicial, dataFinal, percentual, valor));
    }
}
