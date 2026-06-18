package br.com.meusaldomensal.adapters.out.persistence;

import br.com.meusaldomensal.adapters.out.persistence.mapper.SalaryPersistenceMapper;
import br.com.meusaldomensal.adapters.out.persistence.repository.SalaryJpaRepository;
import br.com.meusaldomensal.application.ports.out.SalaryRepositoryPort;
import br.com.meusaldomensal.domain.model.Salary;
import br.com.meusaldomensal.infrastructure.tenant.TenantSchemaGuard;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class SalaryPersistenceAdapter implements SalaryRepositoryPort {

    private final SalaryJpaRepository salaryJpaRepository;
    private final TenantSchemaGuard tenantSchemaGuard;

    public SalaryPersistenceAdapter(SalaryJpaRepository salaryJpaRepository, TenantSchemaGuard tenantSchemaGuard) {
        this.salaryJpaRepository = salaryJpaRepository;
        this.tenantSchemaGuard = tenantSchemaGuard;
    }

    @Override
    public Salary save(Salary salary) {
        tenantSchemaGuard.applyCurrentTenant();
        return SalaryPersistenceMapper.toDomain(salaryJpaRepository.save(SalaryPersistenceMapper.toEntity(salary)));
    }

    @Override
    public List<Salary> findByMonthAndYear(Integer month, Integer year) {
        tenantSchemaGuard.applyCurrentTenant();
        return salaryJpaRepository.findByMonthAndYear(month, year).stream()
                .map(SalaryPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Salary> findById(UUID id) {
        tenantSchemaGuard.applyCurrentTenant();
        return salaryJpaRepository.findById(id).map(SalaryPersistenceMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        tenantSchemaGuard.applyCurrentTenant();
        salaryJpaRepository.deleteById(id);
    }
}
