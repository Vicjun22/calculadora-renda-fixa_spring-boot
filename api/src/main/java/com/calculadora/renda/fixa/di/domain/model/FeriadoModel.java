package com.calculadora.renda.fixa.di.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeriadoModel {

    private LocalDate date;
    private String name;
    private String type;
}
