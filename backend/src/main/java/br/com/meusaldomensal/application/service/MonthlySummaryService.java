package br.com.meusaldomensal.application.service;

import br.com.meusaldomensal.application.exception.BusinessException;
import br.com.meusaldomensal.application.ports.in.DashboardUseCase;
import br.com.meusaldomensal.application.ports.in.MonthlySummaryUseCase;
import br.com.meusaldomensal.application.ports.out.MonthlySummaryRepositoryPort;
import br.com.meusaldomensal.application.query.DashboardView;
import br.com.meusaldomensal.domain.model.MonthlySummary;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MonthlySummaryService implements MonthlySummaryUseCase {

    private final DashboardUseCase dashboardUseCase;
    private final MonthlySummaryRepositoryPort monthlySummaryRepository;

    public MonthlySummaryService(
            DashboardUseCase dashboardUseCase,
            MonthlySummaryRepositoryPort monthlySummaryRepository) {
        this.dashboardUseCase = dashboardUseCase;
        this.monthlySummaryRepository = monthlySummaryRepository;
    }

    @Override
    @Transactional
    public MonthlySummary closeMonth(Integer month, Integer year) {
        DashboardView dashboard = dashboardUseCase.getDashboard(month, year);
        boolean hasEntries = dashboard.totalIncome().compareTo(BigDecimal.ZERO) > 0
                || dashboard.totalExpenses().compareTo(BigDecimal.ZERO) > 0;
        if (!hasEntries) {
            throw new BusinessException("Nao e permitido fechar mes sem lancamentos.");
        }

        UUID summaryId = monthlySummaryRepository.findByMonthAndYear(month, year)
                .map(MonthlySummary::id)
                .orElseGet(UUID::randomUUID);

        return monthlySummaryRepository.save(new MonthlySummary(
                summaryId,
                month,
                year,
                dashboard.totalSalary(),
                dashboard.totalExtraIncome(),
                dashboard.totalIncome(),
                dashboard.totalExpenses(),
                dashboard.totalPaid(),
                dashboard.totalPending(),
                dashboard.remainingBalance(),
                dashboard.committedPercentage(),
                LocalDateTime.now()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonthlySummary> findAll(Integer year) {
        List<MonthlySummary> summaries = year == null
                ? monthlySummaryRepository.findAll()
                : monthlySummaryRepository.findByYear(year);
        return summaries.stream()
                .sorted(Comparator.comparing(MonthlySummary::year).reversed()
                        .thenComparing(Comparator.comparing(MonthlySummary::month).reversed()))
                .toList();
    }
}
