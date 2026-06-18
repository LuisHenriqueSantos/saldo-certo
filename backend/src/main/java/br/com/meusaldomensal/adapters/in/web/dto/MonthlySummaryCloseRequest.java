package br.com.meusaldomensal.adapters.in.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MonthlySummaryCloseRequest(
        @NotNull(message = "Mes e obrigatorio.")
        @Min(value = 1, message = "Mes deve estar entre 1 e 12.")
        @Max(value = 12, message = "Mes deve estar entre 1 e 12.")
        Integer month,
        @NotNull(message = "Ano e obrigatorio.")
        @Min(value = 2000, message = "Ano invalido.")
        Integer year) {
}
