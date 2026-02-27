package com.expense.saas.security;

import com.expense.saas.domain.enums.UserRole;

import java.util.UUID;

public record AuthenticatedUser(
        UUID userId,
        UserRole role,
        String email
) {
}
