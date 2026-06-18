package br.com.meusaldomensal.application.query;

import br.com.meusaldomensal.domain.enums.ExpenseStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record DashboardView(
        Integer month,
        Integer year,
        BigDecimal totalSalary,
        BigDecimal totalExtraIncome,
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal totalPaid,
        BigDecimal totalPending,
        BigDecimal remainingBalance,
        BigDecimal committedPercentage,
        List<ExpenseByCategory> expensesByCategory,
        List<IncomeVsExpense> incomeVsExpense,
        List<Bill> upcomingBills,
        List<Bill> pendingBills,
        List<Bill> paidBills) {

    public record ExpenseByCategory(
            UUID categoryId,
            String categoryName,
            String color,
            BigDecimal total) {
    }

    public record IncomeVsExpense(
            String name,
            BigDecimal value) {
    }

    public record Bill(
            UUID id,
            UUID categoryId,
            String categoryName,
            String description,
            BigDecimal amount,
            LocalDate dueDate,
            LocalDate paymentDate,
            ExpenseStatus status) {
    }
}
