package com.calculadora.renda.fixa.di.util;

import com.calculadora.renda.fixa.di.domain.model.FeriadoModel;
import com.calculadora.renda.fixa.di.exception.InvalidParameterException;
import com.calculadora.renda.fixa.di.exception.NotFoundParameterException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static com.calculadora.renda.fixa.di.domain.constants.Constants.BRASIL_API_URL;
import static com.calculadora.renda.fixa.di.util.RegrasUtil.parameterNotFound;

@Component
public class ConsultarFeriados {

    private final RestTemplate restTemplate;

    public ConsultarFeriados(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<FeriadoModel> buscarListaDeFeriados(String ano) {
        try {
            String url = BRASIL_API_URL.concat("/feriados/v1/").concat(ano);
            ResponseEntity<FeriadoModel[]> responseEntity = restTemplate.getForEntity(url, FeriadoModel[].class);

            if (Objects.isNull(responseEntity.getBody())) {
                parameterNotFound("Ocorreu um erro! Nenhum feriado encontrado.");
            }

            return List.of(responseEntity.getBody());

        } catch (NotFoundParameterException e) {
            throw e;

        } catch (Exception e) {
            throw new InvalidParameterException("Ocorreu um erro ao consultar a lista de feriados.");
        }
    }
}
