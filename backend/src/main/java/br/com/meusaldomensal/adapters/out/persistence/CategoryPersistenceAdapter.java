package br.com.meusaldomensal.adapters.out.persistence;

import br.com.meusaldomensal.adapters.out.persistence.mapper.CategoryPersistenceMapper;
import br.com.meusaldomensal.adapters.out.persistence.repository.CategoryJpaRepository;
import br.com.meusaldomensal.application.ports.out.CategoryRepositoryPort;
import br.com.meusaldomensal.domain.model.Category;
import br.com.meusaldomensal.infrastructure.tenant.TenantSchemaGuard;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryPersistenceAdapter implements CategoryRepositoryPort {

    private final CategoryJpaRepository categoryJpaRepository;
    private final TenantSchemaGuard tenantSchemaGuard;

    public CategoryPersistenceAdapter(CategoryJpaRepository categoryJpaRepository, TenantSchemaGuard tenantSchemaGuard) {
        this.categoryJpaRepository = categoryJpaRepository;
        this.tenantSchemaGuard = tenantSchemaGuard;
    }

    @Override
    public Category save(Category category) {
        tenantSchemaGuard.applyCurrentTenant();
        return CategoryPersistenceMapper.toDomain(
                categoryJpaRepository.save(CategoryPersistenceMapper.toEntity(category)));
    }

    @Override
    public List<Category> findAll() {
        tenantSchemaGuard.applyCurrentTenant();
        return categoryJpaRepository.findAll().stream()
                .map(CategoryPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Category> findById(UUID id) {
        tenantSchemaGuard.applyCurrentTenant();
        return categoryJpaRepository.findById(id).map(CategoryPersistenceMapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        tenantSchemaGuard.applyCurrentTenant();
        categoryJpaRepository.deleteById(id);
    }
}
