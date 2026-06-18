package br.com.meusaldomensal.application.ports.in.command;

import br.com.meusaldomensal.domain.enums.CategoryType;

public record CategoryCommand(
        String name,
        CategoryType type,
        String color,
        String icon) {
}
