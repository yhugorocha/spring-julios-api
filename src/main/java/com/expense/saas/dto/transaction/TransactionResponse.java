package com.expense.saas.dto.transaction;

import com.expense.saas.domain.ExpenseTransaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        String description,
        BigDecimal amount,
        LocalDate date,
        UUID categoryId,
        String categoryName,
        UUID createdBy,
        Instant createdAt
) {

    public static TransactionResponse fromEntity(ExpenseTransaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getCategory().getId(),
                transaction.getCategory().getName(),
                transaction.getCreatedBy().getId(),
                transaction.getCreatedAt()
        );
    }
}
