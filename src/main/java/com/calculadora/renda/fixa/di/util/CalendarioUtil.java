package com.calculadora.renda.fixa.di.util;

import com.calculadora.renda.fixa.di.domain.model.FeriadoModel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public class CalendarioUtil {

    private CalendarioUtil() { }

    public static LocalDate ajustarParaDiaUtil(LocalDate data, List<FeriadoModel> feriadosNacionais) {
        while (isFinalDeSemana(data) || isFeriado(data, feriadosNacionais)) {
            data = data.plusDays(1);
        }

        return data;
    }

    private static boolean isFinalDeSemana(LocalDate data) {
        return data.getDayOfWeek() == DayOfWeek.SATURDAY || data.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    private static boolean isFeriado(LocalDate data, List<FeriadoModel> feriados) {
        return feriados.stream().anyMatch(feriado -> feriado.getDate().equals(data));
    }
}
