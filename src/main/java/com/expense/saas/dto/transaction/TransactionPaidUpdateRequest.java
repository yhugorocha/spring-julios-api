package com.expense.saas.dto.transaction;

import jakarta.validation.constraints.NotNull;

public record TransactionPaidUpdateRequest(
        @NotNull(message = "Campo paid e obrigatorio")
        Boolean paid
) {
}