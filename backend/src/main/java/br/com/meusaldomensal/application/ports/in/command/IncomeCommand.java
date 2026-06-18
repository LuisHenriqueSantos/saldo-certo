package br.com.meusaldomensal.application.ports.in.command;

import br.com.meusaldomensal.domain.enums.IncomeType;
import java.math.BigDecimal;
import java.time.LocalDate;

public record IncomeCommand(
        String description,
        BigDecimal amount,
        LocalDate incomeDate,
        IncomeType type,
        Integer month,
        Integer year) {
}
