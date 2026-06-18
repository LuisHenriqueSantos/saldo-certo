package br.com.meusaldomensal.adapters.out.persistence;

import br.com.meusaldomensal.adapters.out.persistence.mapper.IncomePersistenceMapper;
import br.com.meusaldomensal.adapters.out.persistence.repository.IncomeJpaRepository;
import br.com.meusaldomensal.application.ports.out.IncomeRepositoryPort;
import br.com.meusaldomensal.domain.model.Income;
import br.com.meusaldomensal.infrastructure.tenant.TenantSchemaGuard;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class IncomePersistenceAdapter implements IncomeRepositoryPort {

    private final IncomeJpaRepository incomeJpaRepository;
    private final TenantSchemaGuard tenantSchemaGuard;

    public IncomePersistenceAdapter(IncomeJpaRepository incomeJpaRepository, TenantSchemaGuard tenantSchemaGuard) {
        this.incomeJpaRepository = incomeJpaRepository;
        this.tenantSchemaGuard = tenantSchemaGuard;
    }

    @Override
    public Income save(Income income) {
        tenantSchemaGuard.applyCurrentTenant();
        return IncomePersistenceMapper.toDomain(incomeJpaRepository.save(IncomePersistenceMapper.toEntity(income)));
    }

    @Override
    public List<Income> findByMonthAndYear(Integer month, Integer year) {
        tenantSchemaGuard.applyCurrentTenant();
        return incomeJpaRepository.findByMonthAndYear(month, year).stream()
                .map(IncomePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Income> findById(UUID id) {
        tenantSchemaGuard.applyCurrentTenant();
        return incomeJpaRepository.findById(id).map(IncomePersistenceMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        tenantSchemaGuard.applyCurrentTenant();
        incomeJpaRepository.deleteById(id);
    }
}
