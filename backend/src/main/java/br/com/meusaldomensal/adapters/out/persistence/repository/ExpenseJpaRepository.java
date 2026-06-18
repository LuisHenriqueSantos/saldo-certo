package br.com.meusaldomensal.adapters.out.persistence.repository;

import br.com.meusaldomensal.adapters.out.persistence.entity.ExpenseJpaEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseJpaRepository extends JpaRepository<ExpenseJpaEntity, UUID> {
    List<ExpenseJpaEntity> findByMonthAndYear(Integer month, Integer year);
}
