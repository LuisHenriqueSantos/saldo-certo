package br.com.meusaldomensal.adapters.out.persistence.repository;

import br.com.meusaldomensal.adapters.out.persistence.entity.CategoryJpaEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, UUID> {
}
