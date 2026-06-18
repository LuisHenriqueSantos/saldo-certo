package br.com.meusaldomensal.application.ports.in;

import br.com.meusaldomensal.application.ports.in.command.IncomeCommand;
import br.com.meusaldomensal.domain.model.Income;
import java.util.List;
import java.util.UUID;

public interface IncomeUseCase {
    Income create(IncomeCommand command);
    List<Income> findByMonthAndYear(Integer month, Integer year);
    Income findById(UUID id);
    Income update(UUID id, IncomeCommand command);
    void delete(UUID id);
}
