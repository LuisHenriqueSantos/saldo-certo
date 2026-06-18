package br.com.meusaldomensal.adapters.out.persistence.mapper;

import br.com.meusaldomensal.adapters.out.persistence.entity.CategoryJpaEntity;
import br.com.meusaldomensal.domain.model.Category;

public final class CategoryPersistenceMapper {

    private CategoryPersistenceMapper() {
    }

    public static Category toDomain(CategoryJpaEntity entity) {
        return new Category(
                entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getColor(),
                entity.getIcon(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

    public static CategoryJpaEntity toEntity(Category category) {
        CategoryJpaEntity entity = new CategoryJpaEntity();
        entity.setId(category.id());
        entity.setName(category.name());
        entity.setType(category.type());
        entity.setColor(category.color());
        entity.setIcon(category.icon());
        entity.setCreatedAt(category.createdAt());
        entity.setUpdatedAt(category.updatedAt());
        return entity;
    }
}
