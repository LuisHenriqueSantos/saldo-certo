package br.com.meusaldomensal.adapters.out.persistence.mapper;

import br.com.meusaldomensal.adapters.out.persistence.entity.CategoryJpaEntity;
import br.com.meusaldomensal.adapters.out.persistence.entity.ExpenseJpaEntity;
import br.com.meusaldomensal.domain.model.Expense;

public final class ExpensePersistenceMapper {

    private ExpensePersistenceMapper() {
    }

    public static Expense toDomain(ExpenseJpaEntity entity) {
        return new Expense(
                entity.getId(),
                CategoryPersistenceMapper.toDomain(entity.getCategory()),
                entity.getDescription(),
                entity.getAmount(),
                entity.getDueDate(),
                entity.getPaymentDate(),
                entity.getStatus(),
                entity.getType(),
                entity.getInstallmentNumber(),
                entity.getTotalInstallments(),
                entity.getMonth(),
                entity.getYear(),
                entity.getNotes(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static ExpenseJpaEntity toEntity(Expense expense, CategoryJpaEntity category) {
        ExpenseJpaEntity entity = new ExpenseJpaEntity();
        entity.setId(expense.id());
        entity.setCategory(category);
        entity.setDescription(expense.description());
        entity.setAmount(expense.amount());
        entity.setDueDate(expense.dueDate());
        entity.setPaymentDate(expense.paymentDate());
        entity.setStatus(expense.status());
        entity.setType(expense.type());
        entity.setInstallmentNumber(expense.installmentNumber());
        entity.setTotalInstallments(expense.totalInstallments());
        entity.setMonth(expense.month());
        entity.setYear(expense.year());
        entity.setNotes(expense.notes());
        entity.setCreatedAt(expense.createdAt());
        entity.setUpdatedAt(expense.updatedAt());
        return entity;
    }
}
