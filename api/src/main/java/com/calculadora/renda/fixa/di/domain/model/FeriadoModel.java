package com.calculadora.renda.fixa.di.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class FeriadoModel {

    private LocalDate date;
    private String name;
    private String type;
}
