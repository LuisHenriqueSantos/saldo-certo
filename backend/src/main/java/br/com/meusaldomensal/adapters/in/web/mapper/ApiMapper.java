package br.com.meusaldomensal.adapters.in.web.mapper;

import br.com.meusaldomensal.adapters.in.web.dto.CategoryRequest;
import br.com.meusaldomensal.adapters.in.web.dto.CategoryResponse;
import br.com.meusaldomensal.adapters.in.web.dto.ExpenseRequest;
import br.com.meusaldomensal.adapters.in.web.dto.ExpenseResponse;
import br.com.meusaldomensal.adapters.in.web.dto.IncomeRequest;
import br.com.meusaldomensal.adapters.in.web.dto.IncomeResponse;
import br.com.meusaldomensal.adapters.in.web.dto.MonthlySummaryResponse;
import br.com.meusaldomensal.adapters.in.web.dto.SalaryRequest;
import br.com.meusaldomensal.adapters.in.web.dto.SalaryResponse;
import br.com.meusaldomensal.application.ports.in.command.CategoryCommand;
import br.com.meusaldomensal.application.ports.in.command.ExpenseCommand;
import br.com.meusaldomensal.application.ports.in.command.IncomeCommand;
import br.com.meusaldomensal.application.ports.in.command.SalaryCommand;
import br.com.meusaldomensal.domain.model.Category;
import br.com.meusaldomensal.domain.model.Expense;
import br.com.meusaldomensal.domain.model.Income;
import br.com.meusaldomensal.domain.model.MonthlySummary;
import br.com.meusaldomensal.domain.model.Salary;

public final class ApiMapper {

    private ApiMapper() {
    }

    public static CategoryCommand toCommand(CategoryRequest request) {
        return new CategoryCommand(request.name(), request.type(), request.color(), request.icon());
    }

    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.id(),
                category.name(),
                category.type(),
                category.color(),
                category.icon(),
                category.createdAt(),
                category.updatedAt());
    }

    public static SalaryCommand toCommand(SalaryRequest request) {
        return new SalaryCommand(
                request.description(),
                request.amount(),
                request.receivedDate(),
                request.month(),
                request.year());
    }

    public static SalaryResponse toResponse(Salary salary) {
        return new SalaryResponse(
                salary.id(),
                salary.description(),
                salary.amount(),
                salary.receivedDate(),
                salary.month(),
                salary.year(),
                salary.createdAt(),
                salary.updatedAt());
    }

    public static IncomeCommand toCommand(IncomeRequest request) {
        return new IncomeCommand(
                request.description(),
                request.amount(),
                request.incomeDate(),
                request.type(),
                request.month(),
                request.year());
    }

    public static IncomeResponse toResponse(Income income) {
        return new IncomeResponse(
                income.id(),
                income.description(),
                income.amount(),
                income.incomeDate(),
                income.type(),
                income.month(),
                income.year(),
                income.createdAt(),
                income.updatedAt());
    }

    public static ExpenseCommand toCommand(ExpenseRequest request) {
        return new ExpenseCommand(
                request.categoryId(),
                request.description(),
                request.amount(),
                request.dueDate(),
                request.paymentDate(),
                request.status(),
                request.type(),
                request.installmentNumber(),
                request.totalInstallments(),
                request.month(),
                request.year(),
                request.notes());
    }

    public static ExpenseResponse toResponse(Expense expense) {
        return new ExpenseResponse(
                expense.id(),
                expense.category().id(),
                expense.category().name(),
                expense.category().color(),
                expense.description(),
                expense.amount(),
                expense.dueDate(),
                expense.paymentDate(),
                expense.status(),
                expense.type(),
                expense.installmentNumber(),
                expense.totalInstallments(),
                expense.month(),
                expense.year(),
                expense.notes(),
                expense.createdAt(),
                expense.updatedAt());
    }

    public static MonthlySummaryResponse toResponse(MonthlySummary summary) {
        return new MonthlySummaryResponse(
                summary.id(),
                summary.month(),
                summary.year(),
                summary.totalSalary(),
                summary.totalExtraIncome(),
                summary.totalIncome(),
                summary.totalExpenses(),
                summary.totalPaid(),
                summary.totalPending(),
                summary.finalBalance(),
                summary.committedPercentage(),
                summary.closedAt());
    }
}
