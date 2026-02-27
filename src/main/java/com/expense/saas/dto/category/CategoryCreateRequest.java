package com.expense.saas.dto.category;

import com.expense.saas.domain.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest(
        @NotBlank(message = "Category name is required")
        @Size(max = 120, message = "Category name must have at most 120 characters")
        String name,

        @NotNull(message = "Category type is required")
        CategoryType type
) {
}
