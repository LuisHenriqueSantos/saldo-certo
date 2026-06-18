package br.com.meusaldomensal.adapters.out.persistence.repository.auth;

import br.com.meusaldomensal.adapters.out.persistence.entity.auth.TenantAccountJpaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantAccountJpaRepository extends JpaRepository<TenantAccountJpaEntity, UUID> {
    Optional<TenantAccountJpaEntity> findByOwnerUserId(UUID ownerUserId);
}
