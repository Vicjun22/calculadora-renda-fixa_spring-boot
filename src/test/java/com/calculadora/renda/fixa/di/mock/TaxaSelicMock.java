package com.calculadora.renda.fixa.di.mock;

import com.calculadora.renda.fixa.di.domain.model.TaxaSelicModel;

import java.util.List;

public class TaxaSelicMock {

    private TaxaSelicMock() { }

    public static List<TaxaSelicModel> listaTaxaSelicMock() {
        return List.of(
                new TaxaSelicModel("02/01/2025", "0.043739"),
                new TaxaSelicModel("03/01/2025", "0.043739"),
                new TaxaSelicModel("04/01/2025", "0.043739"),
                new TaxaSelicModel("05/01/2025", "0.043739")
        );
    }
}
