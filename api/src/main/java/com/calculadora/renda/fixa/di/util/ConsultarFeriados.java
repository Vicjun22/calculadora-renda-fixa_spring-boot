package com.calculadora.renda.fixa.di.util;

import com.calculadora.renda.fixa.di.domain.model.FeriadoModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static com.calculadora.renda.fixa.di.domain.constants.Constants.BRASIL_API_URL;

public class ConsultarFeriados {

    private ConsultarFeriados() { }

    public static List<FeriadoModel> buscarListaDeFeriados(String ano) {

        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = BRASIL_API_URL.concat("/feriados/v1/").concat(ano);
            ResponseEntity<FeriadoModel[]> responseEntity = restTemplate.getForEntity(url, FeriadoModel[].class);

            if (Objects.isNull(responseEntity.getBody())) {
                throw new RuntimeException("Ocorreu um erro! Nenhum feriado encontrado.");
            }

            return List.of(responseEntity.getBody());

        } catch (Exception e) {
            throw new RuntimeException("Ocorreu um erro ao consultar a lista de feriados.");
        }
    }
}
