package br.com.meusaldomensal.application.service;

import br.com.meusaldomensal.application.exception.ValidationException;
import java.math.BigDecimal;

final class ValidationUtils {

    private ValidationUtils() {
    }

    static void requireMonthYear(Integer month, Integer year) {
        if (month == null || month < 1 || month > 12) {
            throw new ValidationException("Mes invalido. Informe um valor entre 1 e 12.");
        }
        if (year == null || year < 2000 || year > 2100) {
            throw new ValidationException("Ano invalido. Informe um valor entre 2000 e 2100.");
        }
    }

    static void requirePositive(BigDecimal amount, String fieldName) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(fieldName + " deve ser maior que zero.");
        }
    }

    static void requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " e obrigatorio.");
        }
    }
}
