package br.com.meusaldomensal.application.ports.in.command;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SalaryCommand(
        String description,
        BigDecimal amount,
        LocalDate receivedDate,
        Integer month,
        Integer year) {
}
