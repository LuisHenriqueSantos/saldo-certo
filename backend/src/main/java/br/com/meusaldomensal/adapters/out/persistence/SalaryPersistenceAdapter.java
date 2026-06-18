package br.com.meusaldomensal.adapters.out.persistence;

import br.com.meusaldomensal.adapters.out.persistence.mapper.SalaryPersistenceMapper;
import br.com.meusaldomensal.adapters.out.persistence.repository.SalaryJpaRepository;
import br.com.meusaldomensal.application.ports.out.SalaryRepositoryPort;
import br.com.meusaldomensal.domain.model.Salary;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class SalaryPersistenceAdapter implements SalaryRepositoryPort {

    private final SalaryJpaRepository salaryJpaRepository;

    public SalaryPersistenceAdapter(SalaryJpaRepository salaryJpaRepository) {
        this.salaryJpaRepository = salaryJpaRepository;
    }

    @Override
    public Salary save(Salary salary) {
        return SalaryPersistenceMapper.toDomain(salaryJpaRepository.save(SalaryPersistenceMapper.toEntity(salary)));
    }

    @Override
    public List<Salary> findByMonthAndYear(Integer month, Integer year) {
        return salaryJpaRepository.findByMonthAndYear(month, year).stream()
                .map(SalaryPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Salary> findById(UUID id) {
        return salaryJpaRepository.findById(id).map(SalaryPersistenceMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        salaryJpaRepository.deleteById(id);
    }
}
