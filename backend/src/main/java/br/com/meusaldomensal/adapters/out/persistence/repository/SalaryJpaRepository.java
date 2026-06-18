package br.com.meusaldomensal.adapters.out.persistence.repository;

import br.com.meusaldomensal.adapters.out.persistence.entity.SalaryJpaEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaryJpaRepository extends JpaRepository<SalaryJpaEntity, UUID> {
    List<SalaryJpaEntity> findByMonthAndYear(Integer month, Integer year);
}
