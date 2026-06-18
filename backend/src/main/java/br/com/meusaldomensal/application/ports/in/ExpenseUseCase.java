package br.com.meusaldomensal.application.ports.in;

import br.com.meusaldomensal.application.ports.in.command.ExpenseCommand;
import br.com.meusaldomensal.domain.model.Expense;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ExpenseUseCase {
    Expense create(ExpenseCommand command);
    List<Expense> findByMonthAndYear(Integer month, Integer year);
    Expense findById(UUID id);
    Expense update(UUID id, ExpenseCommand command);
    Expense markAsPaid(UUID id, LocalDate paymentDate);
    Expense markAsPending(UUID id);
    void delete(UUID id);
}
