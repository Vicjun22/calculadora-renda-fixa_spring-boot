package com.calculadora.renda.fixa.di.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxaSelicModel {

    private String data;
    private String valor;
}
