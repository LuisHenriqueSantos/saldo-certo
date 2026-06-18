package br.com.meusaldomensal.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MonthlySummary(
        UUID id,
        Integer month,
        Integer year,
        BigDecimal totalSalary,
        BigDecimal totalExtraIncome,
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal totalPaid,
        BigDecimal totalPending,
        BigDecimal finalBalance,
        BigDecimal committedPercentage,
        LocalDateTime closedAt) {
}
