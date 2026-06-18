package br.com.meusaldomensal.application.service;

import br.com.meusaldomensal.application.ports.in.DashboardUseCase;
import br.com.meusaldomensal.application.ports.out.ExpenseRepositoryPort;
import br.com.meusaldomensal.application.ports.out.IncomeRepositoryPort;
import br.com.meusaldomensal.application.ports.out.SalaryRepositoryPort;
import br.com.meusaldomensal.application.query.DashboardView;
import br.com.meusaldomensal.domain.enums.ExpenseStatus;
import br.com.meusaldomensal.domain.model.Expense;
import br.com.meusaldomensal.domain.model.Income;
import br.com.meusaldomensal.domain.model.Salary;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService implements DashboardUseCase {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private final SalaryRepositoryPort salaryRepository;
    private final IncomeRepositoryPort incomeRepository;
    private final ExpenseRepositoryPort expenseRepository;

    public DashboardService(
            SalaryRepositoryPort salaryRepository,
            IncomeRepositoryPort incomeRepository,
            ExpenseRepositoryPort expenseRepository) {
        this.salaryRepository = salaryRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardView getDashboard(Integer month, Integer year) {
        ValidationUtils.requireMonthYear(month, year);
        List<Salary> salaries = salaryRepository.findByMonthAndYear(month, year);
        List<Income> incomes = incomeRepository.findByMonthAndYear(month, year);
        List<Expense> expenses = expenseRepository.findByMonthAndYear(month, year).stream()
                .map(this::withEffectiveStatus)
                .toList();

        BigDecimal totalSalary = salaries.stream().map(Salary::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalExtraIncome = incomes.stream().map(Income::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalIncome = totalSalary.add(totalExtraIncome);
        BigDecimal totalExpenses = expenses.stream().map(Expense::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPaid = expenses.stream()
                .filter(expense -> expense.status() == ExpenseStatus.PAID)
                .map(Expense::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalPending = totalExpenses.subtract(totalPaid);
        BigDecimal committedPercentage = BigDecimal.ZERO;
        if (totalIncome.compareTo(BigDecimal.ZERO) > 0) {
            committedPercentage = totalExpenses
                    .divide(totalIncome, 4, RoundingMode.HALF_UP)
                    .multiply(HUNDRED)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return new DashboardView(
                month,
                year,
                totalSalary,
                totalExtraIncome,
                totalIncome,
                totalExpenses,
                totalPaid,
                totalPending,
                totalIncome.subtract(totalExpenses),
                committedPercentage,
                expensesByCategory(expenses),
                List.of(
                        new DashboardView.IncomeVsExpense("Receitas", totalIncome),
                        new DashboardView.IncomeVsExpense("Despesas", totalExpenses)),
                upcomingBills(expenses),
                billList(expenses.stream()
                        .filter(expense -> expense.status() != ExpenseStatus.PAID)
                        .sorted(Comparator.comparing(Expense::dueDate))
                        .toList()),
                billList(expenses.stream()
                        .filter(expense -> expense.status() == ExpenseStatus.PAID)
                        .sorted(Comparator.comparing(Expense::paymentDate, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                        .toList()));
    }

    private List<DashboardView.ExpenseByCategory> expensesByCategory(List<Expense> expenses) {
        Map<String, List<Expense>> grouped = expenses.stream()
                .collect(Collectors.groupingBy(expense -> expense.category().id().toString()));
        return grouped.values().stream()
                .map(group -> {
                    Expense first = group.get(0);
                    BigDecimal total = group.stream().map(Expense::amount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new DashboardView.ExpenseByCategory(
                            first.category().id(),
                            first.category().name(),
                            first.category().color(),
                            total);
                })
                .sorted(Comparator.comparing(DashboardView.ExpenseByCategory::total).reversed())
                .toList();
    }

    private List<DashboardView.Bill> upcomingBills(List<Expense> expenses) {
        LocalDate today = LocalDate.now();
        LocalDate limit = today.plusDays(7);
        return billList(expenses.stream()
                .filter(expense -> expense.status() != ExpenseStatus.PAID)
                .filter(expense -> !expense.dueDate().isBefore(today) && !expense.dueDate().isAfter(limit))
                .sorted(Comparator.comparing(Expense::dueDate))
                .toList());
    }

    private List<DashboardView.Bill> billList(List<Expense> expenses) {
        return expenses.stream()
                .map(expense -> new DashboardView.Bill(
                        expense.id(),
                        expense.category().id(),
                        expense.category().name(),
                        expense.description(),
                        expense.amount(),
                        expense.dueDate(),
                        expense.paymentDate(),
                        expense.status()))
                .toList();
    }

    private Expense withEffectiveStatus(Expense expense) {
        if (expense.status() != ExpenseStatus.PAID && expense.dueDate().isBefore(LocalDate.now())) {
            return new Expense(
                    expense.id(),
                    expense.category(),
                    expense.description(),
                    expense.amount(),
                    expense.dueDate(),
                    expense.paymentDate(),
                    ExpenseStatus.OVERDUE,
                    expense.type(),
                    expense.installmentNumber(),
                    expense.totalInstallments(),
                    expense.month(),
                    expense.year(),
                    expense.notes(),
                    expense.createdAt(),
                    expense.updatedAt());
        }
        return expense;
    }
}
