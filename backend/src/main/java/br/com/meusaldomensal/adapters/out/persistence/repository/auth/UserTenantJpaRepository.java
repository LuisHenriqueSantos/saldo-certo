package br.com.meusaldomensal.adapters.out.persistence.repository.auth;

import br.com.meusaldomensal.adapters.out.persistence.entity.auth.UserTenantJpaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTenantJpaRepository extends JpaRepository<UserTenantJpaEntity, UUID> {
    Optional<UserTenantJpaEntity> findByUserIdAndTenantId(UUID userId, UUID tenantId);
}
