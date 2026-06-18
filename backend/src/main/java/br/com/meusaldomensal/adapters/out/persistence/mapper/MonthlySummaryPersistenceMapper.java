package br.com.meusaldomensal.adapters.out.persistence.mapper;

import br.com.meusaldomensal.adapters.out.persistence.entity.MonthlySummaryJpaEntity;
import br.com.meusaldomensal.domain.model.MonthlySummary;

public final class MonthlySummaryPersistenceMapper {

    private MonthlySummaryPersistenceMapper() {
    }

    public static MonthlySummary toDomain(MonthlySummaryJpaEntity entity) {
        return new MonthlySummary(
                entity.getId(),
                entity.getMonth(),
                entity.getYear(),
                entity.getTotalSalary(),
                entity.getTotalExtraIncome(),
                entity.getTotalIncome(),
                entity.getTotalExpenses(),
                entity.getTotalPaid(),
                entity.getTotalPending(),
                entity.getFinalBalance(),
                entity.getCommittedPercentage(),
                entity.getClosedAt());
    }

    public static MonthlySummaryJpaEntity toEntity(MonthlySummary summary) {
        MonthlySummaryJpaEntity entity = new MonthlySummaryJpaEntity();
        entity.setId(summary.id());
        entity.setMonth(summary.month());
        entity.setYear(summary.year());
        entity.setTotalSalary(summary.totalSalary());
        entity.setTotalExtraIncome(summary.totalExtraIncome());
        entity.setTotalIncome(summary.totalIncome());
        entity.setTotalExpenses(summary.totalExpenses());
        entity.setTotalPaid(summary.totalPaid());
        entity.setTotalPending(summary.totalPending());
        entity.setFinalBalance(summary.finalBalance());
        entity.setCommittedPercentage(summary.committedPercentage());
        entity.setClosedAt(summary.closedAt());
        return entity;
    }
}
