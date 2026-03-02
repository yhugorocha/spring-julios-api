package com.expense.saas.dto.user;

import com.expense.saas.domain.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "Nome é obrigatório")
        @Size(max = 120, message = "Nome deve ter no máximo 120 caracteres")
        String name,

        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail deve ser válido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 8, max = 120, message = "Senha deve ter entre 8 e 120 caracteres")
        String password,

        @NotNull(message = "Perfil é obrigatório")
        UserRole role
) {
}
