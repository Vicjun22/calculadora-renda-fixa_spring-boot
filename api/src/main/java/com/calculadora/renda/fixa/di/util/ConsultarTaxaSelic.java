package com.calculadora.renda.fixa.di.util;

import com.calculadora.renda.fixa.di.domain.model.TaxaSelicModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static com.calculadora.renda.fixa.di.domain.constants.Constants.BANCO_CENTRAL_DO_BRASIL_CONSULTA_SELIC_URL;

public class ConsultarTaxaSelic {

    private ConsultarTaxaSelic() { }

    public static List<TaxaSelicModel> buscarListaDasTaxasSelic(LocalDate dataInicio, LocalDate dataFim) {

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataInicial = "&dataInicial=".concat(dataInicio.format(formatter));
            String dataFinal = "&dataFinal=".concat(dataFim.format(formatter));

            RestTemplate restTemplate = new RestTemplate();
            String url = BANCO_CENTRAL_DO_BRASIL_CONSULTA_SELIC_URL.concat(dataInicial).concat(dataFinal);
            ResponseEntity<TaxaSelicModel[]> responseEntity = restTemplate.getForEntity(url, TaxaSelicModel[].class);

            if (Objects.isNull(responseEntity.getBody())) {
                throw new RuntimeException("Ocorreu um erro! Nenhuma taxa encontrada.");
            }

            return List.of(responseEntity.getBody());

        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao consultar a lista de taxas SELIC.");
        }
    }
}
