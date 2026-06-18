package br.com.meusaldomensal.adapters.out.persistence;

import br.com.meusaldomensal.adapters.out.persistence.entity.CategoryJpaEntity;
import br.com.meusaldomensal.adapters.out.persistence.mapper.ExpensePersistenceMapper;
import br.com.meusaldomensal.adapters.out.persistence.repository.CategoryJpaRepository;
import br.com.meusaldomensal.adapters.out.persistence.repository.ExpenseJpaRepository;
import br.com.meusaldomensal.application.ports.out.ExpenseRepositoryPort;
import br.com.meusaldomensal.domain.model.Expense;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class ExpensePersistenceAdapter implements ExpenseRepositoryPort {

    private final ExpenseJpaRepository expenseJpaRepository;
    private final CategoryJpaRepository categoryJpaRepository;

    public ExpensePersistenceAdapter(
            ExpenseJpaRepository expenseJpaRepository,
            CategoryJpaRepository categoryJpaRepository) {
        this.expenseJpaRepository = expenseJpaRepository;
        this.categoryJpaRepository = categoryJpaRepository;
    }

    @Override
    public Expense save(Expense expense) {
        CategoryJpaEntity category = categoryJpaRepository.getReferenceById(expense.category().id());
        return ExpensePersistenceMapper.toDomain(
                expenseJpaRepository.save(ExpensePersistenceMapper.toEntity(expense, category)));
    }

    @Override
    public List<Expense> findByMonthAndYear(Integer month, Integer year) {
        return expenseJpaRepository.findByMonthAndYear(month, year).stream()
                .map(ExpensePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Expense> findById(UUID id) {
        return expenseJpaRepository.findById(id).map(ExpensePersistenceMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        expenseJpaRepository.deleteById(id);
    }
}
