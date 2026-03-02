package com.expense.saas.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionCreateRequest(
        @NotBlank(message = "Descrição é obrigatória")
        @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
        String description,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", inclusive = true, message = "Valor deve ser maior que zero")
        BigDecimal amount,

        @NotNull(message = "Data é obrigatória")
        LocalDate date,

        @NotNull(message = "ID da categoria é obrigatório")
        UUID categoryId
) {
}
