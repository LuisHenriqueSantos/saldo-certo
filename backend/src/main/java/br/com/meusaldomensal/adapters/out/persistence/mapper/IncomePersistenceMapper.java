package br.com.meusaldomensal.adapters.out.persistence.mapper;

import br.com.meusaldomensal.adapters.out.persistence.entity.IncomeJpaEntity;
import br.com.meusaldomensal.domain.model.Income;

public final class IncomePersistenceMapper {

    private IncomePersistenceMapper() {
    }

    public static Income toDomain(IncomeJpaEntity entity) {
        return new Income(
                entity.getId(),
                entity.getDescription(),
                entity.getAmount(),
                entity.getIncomeDate(),
                entity.getType(),
                entity.getMonth(),
                entity.getYear(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static IncomeJpaEntity toEntity(Income income) {
        IncomeJpaEntity entity = new IncomeJpaEntity();
        entity.setId(income.id());
        entity.setDescription(income.description());
        entity.setAmount(income.amount());
        entity.setIncomeDate(income.incomeDate());
        entity.setType(income.type());
        entity.setMonth(income.month());
        entity.setYear(income.year());
        entity.setCreatedAt(income.createdAt());
        entity.setUpdatedAt(income.updatedAt());
        return entity;
    }
}
