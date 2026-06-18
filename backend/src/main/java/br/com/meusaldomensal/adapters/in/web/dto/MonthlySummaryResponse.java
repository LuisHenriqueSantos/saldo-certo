package br.com.meusaldomensal.adapters.in.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record MonthlySummaryResponse(
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
