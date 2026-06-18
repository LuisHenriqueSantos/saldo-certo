package br.com.meusaldomensal.adapters.in.web.dto;

import br.com.meusaldomensal.domain.enums.CategoryType;
import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        CategoryType type,
        String color,
        String icon,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
