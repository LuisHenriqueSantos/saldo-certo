package br.com.meusaldomensal.application.ports.out;

import br.com.meusaldomensal.domain.model.Income;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IncomeRepositoryPort {
    Income save(Income income);
    List<Income> findByMonthAndYear(Integer month, Integer year);
    Optional<Income> findById(UUID id);
    void deleteById(UUID id);
}
