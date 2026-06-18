package br.com.meusaldomensal.adapters.out.persistence.repository.auth;

import br.com.meusaldomensal.adapters.out.persistence.entity.auth.UserJpaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    boolean existsByEmail(String email);

    Optional<UserJpaEntity> findByEmail(String email);
}
