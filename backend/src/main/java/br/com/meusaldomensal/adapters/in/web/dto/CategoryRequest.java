package br.com.meusaldomensal.adapters.in.web.dto;

import br.com.meusaldomensal.domain.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(
        @NotBlank(message = "Nome e obrigatorio.")
        String name,
        @NotNull(message = "Tipo e obrigatorio.")
        CategoryType type,
        @NotBlank(message = "Cor e obrigatoria.")
        String color,
        @NotBlank(message = "Icone e obrigatorio.")
        String icon) {
}
