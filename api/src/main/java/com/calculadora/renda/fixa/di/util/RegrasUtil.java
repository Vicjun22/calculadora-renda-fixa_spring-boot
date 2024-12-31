package com.calculadora.renda.fixa.di.util;

import com.calculadora.renda.fixa.di.exception.InvalidParameterException;
import com.calculadora.renda.fixa.di.exception.NotFoundParameterException;

public class RegrasUtil {

    public static void parameterIsBadRequest(String msg) {
        throw new InvalidParameterException(msg);
    }

    public static void parameterNotFound(String msg) {
        throw new NotFoundParameterException(msg);
    }

    public static void validateParameter(boolean invalidRule, String msg) {
        if (invalidRule) {
            throw new InvalidParameterException(msg);
        }
    }

}
