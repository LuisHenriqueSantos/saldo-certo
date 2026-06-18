package br.com.meusaldomensal.domain.model;

import br.com.meusaldomensal.domain.enums.CategoryType;
import java.time.LocalDateTime;
import java.util.UUID;

public record Category(
        UUID id,
        String name,
        CategoryType type,
        String color,
        String icon,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
