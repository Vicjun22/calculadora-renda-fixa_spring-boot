package com.calculadora.renda.fixa.di.util;

import com.calculadora.renda.fixa.di.domain.model.TaxaSelicModel;
import com.calculadora.renda.fixa.di.exception.InvalidParameterException;
import com.calculadora.renda.fixa.di.exception.NotFoundParameterException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.calculadora.renda.fixa.di.domain.constants.Constants.BANCO_CENTRAL_DO_BRASIL_CONSULTA_SELIC_URL;
import static com.calculadora.renda.fixa.di.util.RegrasUtil.parameterNotFound;

@Component
public class ConsultarTaxaSelic {

    private final RestTemplate restTemplate;

    public ConsultarTaxaSelic(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<TaxaSelicModel> buscarListaDasTaxasSelic(LocalDate dataInicio, LocalDate dataFim) {

        try {
            DateTimeFormatter formatterEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataInicial = "&dataInicial=".concat(dataInicio.format(formatterEntrada));
            String dataFinal = "&dataFinal=".concat(dataFim.format(formatterEntrada));

            String url = BANCO_CENTRAL_DO_BRASIL_CONSULTA_SELIC_URL.concat(dataInicial).concat(dataFinal);
            ResponseEntity<TaxaSelicModel[]> responseEntity = restTemplate.getForEntity(url, TaxaSelicModel[].class);

            if (Objects.isNull(responseEntity.getBody())) {
                parameterNotFound("Ocorreu um erro! Nenhuma taxa encontrada.");
            }

            DateTimeFormatter formatterSaida = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return Arrays.stream(responseEntity.getBody())
                    .peek(taxa -> {
                        LocalDate dataConvertida = LocalDate.parse(taxa.getData(), formatterEntrada);
                        taxa.setData(dataConvertida.format(formatterSaida));
                    })
                    .toList();

        } catch (NotFoundParameterException e) {
            throw e;

        } catch (Exception e) {
            throw new InvalidParameterException("Ocorreu um erro ao consultar a lista de taxas SELIC.");
        }
    }
}
