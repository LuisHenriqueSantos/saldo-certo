package br.com.meusaldomensal.adapters.out.persistence.mapper;

import br.com.meusaldomensal.adapters.out.persistence.entity.SalaryJpaEntity;
import br.com.meusaldomensal.domain.model.Salary;

public final class SalaryPersistenceMapper {

    private SalaryPersistenceMapper() {
    }

    public static Salary toDomain(SalaryJpaEntity entity) {
        return new Salary(
                entity.getId(),
                entity.getDescription(),
                entity.getAmount(),
                entity.getReceivedDate(),
                entity.getMonth(),
                entity.getYear(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static SalaryJpaEntity toEntity(Salary salary) {
        SalaryJpaEntity entity = new SalaryJpaEntity();
        entity.setId(salary.id());
        entity.setDescription(salary.description());
        entity.setAmount(salary.amount());
        entity.setReceivedDate(salary.receivedDate());
        entity.setMonth(salary.month());
        entity.setYear(salary.year());
        entity.setCreatedAt(salary.createdAt());
        entity.setUpdatedAt(salary.updatedAt());
        return entity;
    }
}
