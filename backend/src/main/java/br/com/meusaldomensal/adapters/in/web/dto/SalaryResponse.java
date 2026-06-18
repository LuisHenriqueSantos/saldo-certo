package br.com.meusaldomensal.adapters.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record SalaryResponse(
        UUID id,
        String description,
        BigDecimal amount,
        LocalDate receivedDate,
        Integer month,
        Integer year,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
