package br.com.meusaldomensal.adapters.out.persistence.repository;

import br.com.meusaldomensal.adapters.out.persistence.entity.IncomeJpaEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncomeJpaRepository extends JpaRepository<IncomeJpaEntity, UUID> {
    List<IncomeJpaEntity> findByMonthAndYear(Integer month, Integer year);
}
