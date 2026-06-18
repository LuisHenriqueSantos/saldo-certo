package br.com.meusaldomensal.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record Salary(
        UUID id,
        String description,
        BigDecimal amount,
        LocalDate receivedDate,
        Integer month,
        Integer year,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
