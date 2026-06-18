package br.com.meusaldomensal.adapters.out.persistence;

import br.com.meusaldomensal.adapters.out.persistence.mapper.MonthlySummaryPersistenceMapper;
import br.com.meusaldomensal.adapters.out.persistence.repository.MonthlySummaryJpaRepository;
import br.com.meusaldomensal.application.ports.out.MonthlySummaryRepositoryPort;
import br.com.meusaldomensal.domain.model.MonthlySummary;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class MonthlySummaryPersistenceAdapter implements MonthlySummaryRepositoryPort {

    private final MonthlySummaryJpaRepository monthlySummaryJpaRepository;

    public MonthlySummaryPersistenceAdapter(MonthlySummaryJpaRepository monthlySummaryJpaRepository) {
        this.monthlySummaryJpaRepository = monthlySummaryJpaRepository;
    }

    @Override
    public MonthlySummary save(MonthlySummary monthlySummary) {
        return MonthlySummaryPersistenceMapper.toDomain(
                monthlySummaryJpaRepository.save(MonthlySummaryPersistenceMapper.toEntity(monthlySummary)));
    }

    @Override
    public Optional<MonthlySummary> findByMonthAndYear(Integer month, Integer year) {
        return monthlySummaryJpaRepository.findByMonthAndYear(month, year)
                .map(MonthlySummaryPersistenceMapper::toDomain);
    }

    @Override
    public List<MonthlySummary> findAll() {
        return monthlySummaryJpaRepository.findAll().stream()
                .map(MonthlySummaryPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<MonthlySummary> findByYear(Integer year) {
        return monthlySummaryJpaRepository.findByYear(year).stream()
                .map(MonthlySummaryPersistenceMapper::toDomain)
                .toList();
    }
}
