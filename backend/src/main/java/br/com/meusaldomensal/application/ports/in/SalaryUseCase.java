package br.com.meusaldomensal.application.ports.in;

import br.com.meusaldomensal.application.ports.in.command.SalaryCommand;
import br.com.meusaldomensal.domain.model.Salary;
import java.util.List;
import java.util.UUID;

public interface SalaryUseCase {
    Salary create(SalaryCommand command);
    List<Salary> findByMonthAndYear(Integer month, Integer year);
    Salary update(UUID id, SalaryCommand command);
    void delete(UUID id);
}
