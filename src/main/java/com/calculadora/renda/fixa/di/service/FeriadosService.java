package com.calculadora.renda.fixa.di.service;

import com.calculadora.renda.fixa.di.domain.model.FeriadoModel;
import com.calculadora.renda.fixa.di.repository.FeriadosRepository;
import com.calculadora.renda.fixa.di.util.ConsultarFeriados;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeriadosService {

    private final FeriadosRepository repository;

    private final ConsultarFeriados consultarFeriados;

    public void inserirFeriadosReferentesAoAno(String ano) {

        boolean feriadosJaArmazenados = !repository.listarFeriadosPorAno(ano).isEmpty();
        if (!feriadosJaArmazenados) {
            List<FeriadoModel> feriados = Objects.requireNonNull(consultarFeriados.buscarListaDeFeriados(ano));
            repository.inserirFeriadosEmLote(feriados, ano);
        }
    }
}
