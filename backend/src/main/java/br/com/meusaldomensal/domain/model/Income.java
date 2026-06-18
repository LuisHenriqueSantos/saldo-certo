package br.com.meusaldomensal.domain.model;

import br.com.meusaldomensal.domain.enums.IncomeType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Income(
        UUID id,
        String description,
        BigDecimal amount,
        LocalDate incomeDate,
        IncomeType type,
        Integer month,
        Integer year,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
