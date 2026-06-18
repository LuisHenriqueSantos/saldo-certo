package br.com.meusaldomensal.application.ports.out;

import br.com.meusaldomensal.domain.model.Salary;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SalaryRepositoryPort {
    Salary save(Salary salary);
    List<Salary> findByMonthAndYear(Integer month, Integer year);
    Optional<Salary> findById(UUID id);
    void deleteById(UUID id);
}
