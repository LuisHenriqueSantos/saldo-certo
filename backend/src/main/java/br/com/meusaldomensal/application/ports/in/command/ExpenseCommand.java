package br.com.meusaldomensal.application.ports.in.command;

import br.com.meusaldomensal.domain.enums.ExpenseStatus;
import br.com.meusaldomensal.domain.enums.ExpenseType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseCommand(
        UUID categoryId,
        String description,
        BigDecimal amount,
        LocalDate dueDate,
        LocalDate paymentDate,
        ExpenseStatus status,
        ExpenseType type,
        Integer installmentNumber,
        Integer totalInstallments,
        Integer month,
        Integer year,
        String notes) {
}
