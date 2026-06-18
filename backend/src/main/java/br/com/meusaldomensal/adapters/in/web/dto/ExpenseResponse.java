package br.com.meusaldomensal.adapters.in.web.dto;

import br.com.meusaldomensal.domain.enums.ExpenseStatus;
import br.com.meusaldomensal.domain.enums.ExpenseType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseResponse(
        UUID id,
        UUID categoryId,
        String categoryName,
        String categoryColor,
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
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
