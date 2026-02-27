package com.expense.saas.dto.auth;

import com.expense.saas.domain.enums.UserRole;

import java.time.Instant;
import java.util.UUID;

public record AuthResponse(
        String accessToken,
        String tokenType,
        Instant expiresAt,
        UUID userId,
        UserRole role
) {
}
