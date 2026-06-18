package br.com.meusaldomensal.adapters.in.web.dto;

import br.com.meusaldomensal.domain.enums.ExpenseStatus;
import br.com.meusaldomensal.domain.enums.ExpenseType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record ExpenseRequest(
        @NotNull(message = "Categoria e obrigatoria.")
        UUID categoryId,
        @NotBlank(message = "Descricao e obrigatoria.")
        String description,
        @NotNull(message = "Valor e obrigatorio.")
        @Positive(message = "Valor deve ser maior que zero.")
        BigDecimal amount,
        @NotNull(message = "Data de vencimento e obrigatoria.")
        LocalDate dueDate,
        LocalDate paymentDate,
        ExpenseStatus status,
        @NotNull(message = "Tipo e obrigatorio.")
        ExpenseType type,
        Integer installmentNumber,
        Integer totalInstallments,
        @NotNull(message = "Mes e obrigatorio.")
        @Min(value = 1, message = "Mes deve estar entre 1 e 12.")
        @Max(value = 12, message = "Mes deve estar entre 1 e 12.")
        Integer month,
        @NotNull(message = "Ano e obrigatorio.")
        @Min(value = 2000, message = "Ano invalido.")
        Integer year,
        String notes) {
}
