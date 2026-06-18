package br.com.meusaldomensal.adapters.out.persistence;

import br.com.meusaldomensal.adapters.out.persistence.mapper.IncomePersistenceMapper;
import br.com.meusaldomensal.adapters.out.persistence.repository.IncomeJpaRepository;
import br.com.meusaldomensal.application.ports.out.IncomeRepositoryPort;
import br.com.meusaldomensal.domain.model.Income;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class IncomePersistenceAdapter implements IncomeRepositoryPort {

    private final IncomeJpaRepository incomeJpaRepository;

    public IncomePersistenceAdapter(IncomeJpaRepository incomeJpaRepository) {
        this.incomeJpaRepository = incomeJpaRepository;
    }

    @Override
    public Income save(Income income) {
        return IncomePersistenceMapper.toDomain(incomeJpaRepository.save(IncomePersistenceMapper.toEntity(income)));
    }

    @Override
    public List<Income> findByMonthAndYear(Integer month, Integer year) {
        return incomeJpaRepository.findByMonthAndYear(month, year).stream()
                .map(IncomePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Income> findById(UUID id) {
        return incomeJpaRepository.findById(id).map(IncomePersistenceMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        incomeJpaRepository.deleteById(id);
    }
}
