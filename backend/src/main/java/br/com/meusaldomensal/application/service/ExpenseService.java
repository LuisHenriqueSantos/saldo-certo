package br.com.meusaldomensal.application.service;

import br.com.meusaldomensal.application.exception.NotFoundException;
import br.com.meusaldomensal.application.exception.ValidationException;
import br.com.meusaldomensal.application.ports.in.ExpenseUseCase;
import br.com.meusaldomensal.application.ports.in.command.ExpenseCommand;
import br.com.meusaldomensal.application.ports.out.CategoryRepositoryPort;
import br.com.meusaldomensal.application.ports.out.ExpenseRepositoryPort;
import br.com.meusaldomensal.domain.enums.CategoryType;
import br.com.meusaldomensal.domain.enums.ExpenseStatus;
import br.com.meusaldomensal.domain.model.Category;
import br.com.meusaldomensal.domain.model.Expense;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpenseService implements ExpenseUseCase {

    private final ExpenseRepositoryPort expenseRepository;
    private final CategoryRepositoryPort categoryRepository;

    public ExpenseService(ExpenseRepositoryPort expenseRepository, CategoryRepositoryPort categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Expense create(ExpenseCommand command) {
        validate(command);
        Category category = findExpenseCategory(command.categoryId());
        LocalDateTime now = LocalDateTime.now();
        return expenseRepository.save(new Expense(
                UUID.randomUUID(),
                category,
                command.description().trim(),
                command.amount(),
                command.dueDate(),
                command.paymentDate(),
                normalizeStatus(command.status(), command.paymentDate()),
                command.type(),
                command.installmentNumber(),
                command.totalInstallments(),
                command.month(),
                command.year(),
                normalizeNotes(command.notes()),
                now,
                now));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Expense> findByMonthAndYear(Integer month, Integer year) {
        ValidationUtils.requireMonthYear(month, year);
        return expenseRepository.findByMonthAndYear(month, year).stream()
                .map(this::withEffectiveStatus)
                .sorted(Comparator.comparing(Expense::dueDate))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Expense findById(UUID id) {
        return expenseRepository.findById(id)
                .map(this::withEffectiveStatus)
                .orElseThrow(() -> new NotFoundException("Despesa nao encontrada."));
    }

    @Override
    @Transactional
    public Expense update(UUID id, ExpenseCommand command) {
        validate(command);
        Expense current = expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Despesa nao encontrada."));
        Category category = findExpenseCategory(command.categoryId());
        return expenseRepository.save(new Expense(
                current.id(),
                category,
                command.description().trim(),
                command.amount(),
                command.dueDate(),
                command.paymentDate(),
                normalizeStatus(command.status(), command.paymentDate()),
                command.type(),
                command.installmentNumber(),
                command.totalInstallments(),
                command.month(),
                command.year(),
                normalizeNotes(command.notes()),
                current.createdAt(),
                LocalDateTime.now()));
    }

    @Override
    @Transactional
    public Expense markAsPaid(UUID id, LocalDate paymentDate) {
        Expense current = expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Despesa nao encontrada."));
        LocalDate paidAt = paymentDate == null ? LocalDate.now() : paymentDate;
        return expenseRepository.save(new Expense(
                current.id(),
                current.category(),
                current.description(),
                current.amount(),
                current.dueDate(),
                paidAt,
                ExpenseStatus.PAID,
                current.type(),
                current.installmentNumber(),
                current.totalInstallments(),
                current.month(),
                current.year(),
                current.notes(),
                current.createdAt(),
                LocalDateTime.now()));
    }

    @Override
    @Transactional
    public Expense markAsPending(UUID id) {
        Expense current = expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Despesa nao encontrada."));
        return expenseRepository.save(new Expense(
                current.id(),
                current.category(),
                current.description(),
                current.amount(),
                current.dueDate(),
                null,
                ExpenseStatus.PENDING,
                current.type(),
                current.installmentNumber(),
                current.totalInstallments(),
                current.month(),
                current.year(),
                current.notes(),
                current.createdAt(),
                LocalDateTime.now()));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        expenseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Despesa nao encontrada."));
        expenseRepository.deleteById(id);
    }

    private void validate(ExpenseCommand command) {
        if (command.categoryId() == null) {
            throw new ValidationException("Categoria e obrigatoria para despesa.");
        }
        ValidationUtils.requireText(command.description(), "Descricao da despesa");
        ValidationUtils.requirePositive(command.amount(), "Valor da despesa");
        if (command.dueDate() == null) {
            throw new ValidationException("Data de vencimento e obrigatoria.");
        }
        if (command.type() == null) {
            throw new ValidationException("Tipo da despesa e obrigatorio.");
        }
        ValidationUtils.requireMonthYear(command.month(), command.year());
    }

    private Category findExpenseCategory(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Categoria nao encontrada."));
        if (category.type() != CategoryType.EXPENSE) {
            throw new ValidationException("Categoria deve ser do tipo EXPENSE para despesa.");
        }
        return category;
    }

    private ExpenseStatus normalizeStatus(ExpenseStatus status, LocalDate paymentDate) {
        if (status == null) {
            return paymentDate == null ? ExpenseStatus.PENDING : ExpenseStatus.PAID;
        }
        if (status == ExpenseStatus.PAID && paymentDate == null) {
            return ExpenseStatus.PAID;
        }
        if (status == ExpenseStatus.OVERDUE) {
            return ExpenseStatus.PENDING;
        }
        return status;
    }

    private Expense withEffectiveStatus(Expense expense) {
        if (expense.status() == ExpenseStatus.PAID) {
            return expense;
        }
        if (expense.dueDate().isBefore(LocalDate.now())) {
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

    private String normalizeNotes(String notes) {
        return notes == null || notes.isBlank() ? null : notes.trim();
    }
}
