package com.calculadora.renda.fixa.di.service;

import com.calculadora.renda.fixa.di.domain.model.FeriadoModel;
import com.calculadora.renda.fixa.di.domain.model.TaxaSelicModel;
import com.calculadora.renda.fixa.di.repository.FeriadosRepository;
import com.calculadora.renda.fixa.di.repository.TaxaSelicRepository;
import com.calculadora.renda.fixa.di.util.ConsultarTaxaSelic;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static com.calculadora.renda.fixa.di.util.CalendarioUtil.ajustarParaDiaUtil;

@Service
@RequiredArgsConstructor
public class TaxaSelicService {

    private final FeriadosRepository feriadosRepository;
    private final TaxaSelicRepository taxaSelicRepository;
    private final ConsultarTaxaSelic consultarTaxaSelic;

    public void inserirTaxasSelic() {
        TaxaSelicModel taxaSelicInicial = taxaSelicRepository.consultarUltimaDataInserida();

        List<FeriadoModel> feriados = Objects.requireNonNull(feriadosRepository.listarFeriadosPorAno(String.valueOf(LocalDate.now().getYear())));
        List<FeriadoModel> feriadosNacionais = feriados.stream().filter(feriado -> feriado.getType().equals("national")).toList();
        LocalDate dataFinalEmDiaUtil = ajustarParaDiaUtil(LocalDate.now().minusDays(1), feriadosNacionais);

        if (!taxaSelicInicial.getData().equals(String.valueOf(dataFinalEmDiaUtil))) {
            List<TaxaSelicModel> novosDados = consultarTaxaSelic.buscarListaDasTaxasSelic(LocalDate.parse(taxaSelicInicial.getData()), dataFinalEmDiaUtil);
            novosDados.remove(0);
            taxaSelicRepository.inserirTaxasEmLote(novosDados);
        }
    }
}
