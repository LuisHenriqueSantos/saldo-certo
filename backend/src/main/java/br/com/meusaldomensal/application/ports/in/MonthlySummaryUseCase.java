package br.com.meusaldomensal.application.ports.in;

import br.com.meusaldomensal.domain.model.MonthlySummary;
import java.util.List;

public interface MonthlySummaryUseCase {
    MonthlySummary closeMonth(Integer month, Integer year);
    List<MonthlySummary> findAll(Integer year);
}
