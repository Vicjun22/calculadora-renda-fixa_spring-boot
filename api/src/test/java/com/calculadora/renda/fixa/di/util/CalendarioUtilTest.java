package com.calculadora.renda.fixa.di.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static com.calculadora.renda.fixa.di.util.CalendarioUtil.ajustarParaDiaUtil;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class CalendarioUtilTest {

    @Test
    void ajustarParaDiaUtilQuandoEhFinalDeSemana() {
        LocalDate response = ajustarParaDiaUtil(LocalDate.of(2024, 12, 29), List.of());
        assertEquals(LocalDate.of(2024, 12, 30), response);
    }
}
