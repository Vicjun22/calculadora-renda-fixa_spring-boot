package com.calculadora.renda.fixa.di.mock;

import com.calculadora.renda.fixa.di.domain.model.FeriadoModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FeriadoModelMock {

    private FeriadoModelMock() { }

    public static FeriadoModel toDomain(LocalDate date, String name, String type) {
        FeriadoModel domain = new FeriadoModel();
        domain.setDate(date);
        domain.setName(name);
        domain.setType(type);

        return domain;
    }

    public static List<FeriadoModel> listaFeriadosMock() {
        List<FeriadoModel> feriados = new ArrayList<>();

        String[][] feriadosData = {
                {"2024-01-01", "Confraternização mundial", "national"},
                {"2024-02-13", "Carnaval", "national"},
                {"2024-03-29", "Sexta-feira Santa", "national"},
                {"2024-03-31", "Páscoa", "national"},
                {"2024-04-21", "Tiradentes", "national"},
                {"2024-05-01", "Dia do trabalho", "national"},
                {"2024-05-30", "Corpus Christi", "national"},
                {"2024-09-07", "Independência do Brasil", "national"},
                {"2024-10-12", "Nossa Senhora Aparecida", "national"},
                {"2024-11-02", "Finados", "national"},
                {"2024-11-15", "Proclamação da República", "national"},
                {"2024-11-20", "Dia da consciência negra", "national"},
                {"2024-12-25", "Natal", "national"}
        };

        for (String[] feriado : feriadosData) {
            LocalDate date = LocalDate.parse(feriado[0]);
            String name = feriado[1];
            String type = feriado[2];
            feriados.add(toDomain(date, name, type));
        }

        return feriados;
    }
}
