package com.expense.saas.dto.category;

import com.expense.saas.domain.Category;
import com.expense.saas.domain.enums.CategoryType;

import java.time.Instant;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        CategoryType type,
        boolean active,
        Instant createdAt
) {

    public static CategoryResponse fromEntity(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getType(),
                category.isActive(),
                category.getCreatedAt()
        );
    }
}
