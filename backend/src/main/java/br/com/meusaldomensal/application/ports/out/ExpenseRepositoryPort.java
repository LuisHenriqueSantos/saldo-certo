package br.com.meusaldomensal.application.ports.out;

import br.com.meusaldomensal.domain.model.Expense;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExpenseRepositoryPort {
    Expense save(Expense expense);
    List<Expense> findByMonthAndYear(Integer month, Integer year);
    Optional<Expense> findById(UUID id);
    void deleteById(UUID id);
}
