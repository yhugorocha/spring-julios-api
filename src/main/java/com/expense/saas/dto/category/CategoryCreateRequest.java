package com.expense.saas.dto.category;

import com.expense.saas.domain.enums.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoryCreateRequest(
        @NotBlank(message = "Nome da categoria é obrigatório")
        @Size(max = 120, message = "Nome da categoria deve ter no máximo 120 caracteres")
        String name,

        @NotNull(message = "Tipo da categoria é obrigatório")
        CategoryType type
) {
}
