package br.com.meusaldomensal.application.ports.out;

import br.com.meusaldomensal.domain.model.MonthlySummary;
import java.util.List;
import java.util.Optional;

public interface MonthlySummaryRepositoryPort {
    MonthlySummary save(MonthlySummary monthlySummary);
    Optional<MonthlySummary> findByMonthAndYear(Integer month, Integer year);
    List<MonthlySummary> findAll();
    List<MonthlySummary> findByYear(Integer year);
}
