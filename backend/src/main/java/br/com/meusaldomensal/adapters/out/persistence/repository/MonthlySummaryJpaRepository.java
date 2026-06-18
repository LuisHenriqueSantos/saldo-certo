package br.com.meusaldomensal.adapters.out.persistence.repository;

import br.com.meusaldomensal.adapters.out.persistence.entity.MonthlySummaryJpaEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonthlySummaryJpaRepository extends JpaRepository<MonthlySummaryJpaEntity, UUID> {
    Optional<MonthlySummaryJpaEntity> findByMonthAndYear(Integer month, Integer year);
    List<MonthlySummaryJpaEntity> findByYear(Integer year);
}
