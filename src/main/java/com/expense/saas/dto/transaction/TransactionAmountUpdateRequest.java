package com.expense.saas.dto.transaction;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransactionAmountUpdateRequest(
        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", inclusive = true, message = "Valor deve ser maior que zero")
        BigDecimal amount
) {
}
